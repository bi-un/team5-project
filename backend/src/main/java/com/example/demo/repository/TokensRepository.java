package com.example.demo.repository;

import com.example.demo.domain.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokensRepository extends JpaRepository<Tokens, String> {
    // 토큰은 기본 저장(save) 기능만 써도 충분하므로 안은 비워둡니다.
}