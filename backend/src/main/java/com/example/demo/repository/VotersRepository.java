package com.example.demo.repository;

import com.example.demo.domain.Voters;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VotersRepository extends JpaRepository<Voters, Long> {
    // 해시키(성명+주민번호 암호화 데이터)로 유권자를 찾는 메서드
    Optional<Voters> findByHashKey(String hashKey);
}