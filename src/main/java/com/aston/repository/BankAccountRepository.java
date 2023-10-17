package com.aston.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {
    List<BankAccount> findAll();

    @Transactional
    BankAccount findByNumber(String number);

    @Query(value = """
            select * from bank_account where number = :number FOR UPDATE
            """, nativeQuery = true)
    BankAccount findAndLockByNumber(@Param("number") String number);

    @Query(value = """
            select * from bank_account where number in (:fromAccountNumber, :toAccountNumber)
            order by case when number = :fromAccountNumber then 0 else 1 end
            FOR UPDATE
            """, nativeQuery = true)
    List<BankAccount> findAndLockByNumbers(
            @Param("fromAccountNumber") String fromAccountNumber,
            @Param("toAccountNumber") String toAccountNumber
    );
}
