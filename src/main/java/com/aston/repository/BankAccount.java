package com.aston.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BankAccount {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String  number;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer pinCode;

    @Column(nullable = false)
    private Long amount;
}
