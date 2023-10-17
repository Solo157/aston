package com.aston.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "bank_account")
public class BankAccount {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String  number;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer pinCode;

    @Column(nullable = false)
    private Long amount;
}
