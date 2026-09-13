package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tokens")
@Getter @Setter
@NoArgsConstructor
public class Tokens {

    @Id
    @Column(name = "token_uuid")
    private String tokenUuid; // 무작위 난수 문자열 (PK)[cite: 1]

    @Column(nullable = false)
    private String constituency; // 선거구[cite: 1]

    @Column(nullable = false)
    private boolean isUsed = false; // 사용 완료 여부 (기본값 false)[cite: 1]
}