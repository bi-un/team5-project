package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "voters")
@Getter @Setter
@NoArgsConstructor
public class Voters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK[cite: 1]

    @Column(nullable = false, unique = true)
    private String hashKey; // 성명 + 주민등록번호 해시값 (SHA-256)[cite: 1]

    @Column(nullable = false)
    private String constituency; // 선거구[cite: 1]

    @Column(nullable = false)
    private boolean isVoted = false; // 투표 여부 (기본값 false)[cite: 1]
}