package com.aston.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {
    List<BankAccount> findAll();
    Boolean existsBankAccountByNumber(Long number);

    @Lock(PESSIMISTIC_WRITE)
    BankAccount findAndLockByNumber(Long number);

    @Lock(PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BankAccount b WHERE b.number IN (:fromAccountNumber, :toAccountNumber) ORDER BY CASE " +
            "WHEN b.number = :fromAccountNumber THEN 0 " +
            "WHEN b.number = :toAccountNumber THEN 1 " +
            "ELSE 2 " +
            "END")
    List<BankAccount> findAndLockByNumbers(
            @Param("fromAccountNumber") Long fromAccountNumber,
            @Param("toAccountNumber") Long toAccountNumber
    );
}
