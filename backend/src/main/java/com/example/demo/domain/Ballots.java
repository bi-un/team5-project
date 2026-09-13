package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ballots")
@Getter 
@Setter
@NoArgsConstructor
public class Ballots {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ballotId; // PK[cite: 1]

    @Column(nullable = false)
    private String constituency; // 선거구[cite: 1]

    @Column(nullable = false)
    private Long candidateId; // 선택한 후보 번호[cite: 1]
}