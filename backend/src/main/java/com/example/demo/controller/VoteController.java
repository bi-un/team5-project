package com.example.demo.controller;

import com.example.demo.domain.Tokens;
import com.example.demo.domain.Voters;
import com.example.demo.repository.TokensRepository;
import com.example.demo.repository.VotersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/vote")
@CrossOrigin("*")
public class VoteController {

    private final VotersRepository votersRepository;

    private final TokensRepository tokensRepository;

    VoteController(VotersRepository votersRepository, TokensRepository tokensRepository) {
        this.votersRepository = votersRepository;
        this.tokensRepository = tokensRepository;
    }

    //프론트엔드에서 개인정보와 선거구를 입력하고 QR 발급을 요청하는 API
    @PostMapping("/issue-qr")
    public ResponseEntity<?> issueQr(@RequestBody Map<String, String> request) {
        String hashKey = request.get("hashKey");
        String constituency = request.get("constituency");

        //1. 유권자 명부 대조
        Optional<Voters> voterOpt = votersRepository.findByHashKey(hashKey);
        if (voterOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("명부에 등록되지 않은 유권자입니다.");
        }

        Voters voter = voterOpt.get();

        //2. 이중투표(이미 투표했는지) 검증
        if (voter.isVoted()) {
            return ResponseEntity.badRequest().body("이미 투표를 완료하셨습니다.");
        }

        //3. QR코드에 담을 1회용 무작위 난수(UUID) 생성
        String tokenUuid = UUID.randomUUID().toString();

        //추가: 투표 완료
        voter.setVoted(true);
        votersRepository.save(voter);
        
        Tokens token = new Tokens();
        token.setTokenUuid(tokenUuid);
        token.setConstituency(constituency);
        token.setUsed(false); // 초기 상태는 미사용(false)

        // 4. 발급된 토큰을 DB에 저장
        tokensRepository.save(token);

        // 5. 생성된 UUID를 프론트엔드로 반환 (프론트에서 이 값으로 QR 이미지를 그림)
        return ResponseEntity.ok(Map.of("token", tokenUuid));
    }
    
    @PostMapping("/verify-qr")
    public ResponseEntity<?> verifyQr(@RequestBody Map<String, String> request) {
        String tokenUuid = request.get("token");

        // 1. DB에 존재하는 토큰인지 확인
        Optional<Tokens> tokenOpt = tokensRepository.findById(tokenUuid);
        if (tokenOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("유효하지 않은 가짜 QR 코드입니다.");
        }

        Tokens token = tokenOpt.get();

        // 2. 이미 사용된(투표를 마친) 토큰인지 확인
        if (token.isUsed()) {
            return ResponseEntity.badRequest().body("이미 사용 처리된 투표권입니다.");
        }

        //추가: 투표 완료
        token.setUsed(true);
        tokensRepository.save(token);

        //3. 프론트엔드에 통과 메시지와 선거구 정보 전달
        return ResponseEntity.ok(Map.of(
                "message", "QR 인증 성공",
                "constituency", token.getConstituency()
        ));
    }
}