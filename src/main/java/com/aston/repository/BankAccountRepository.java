package com.aston.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {
    List<BankAccount> findAll();

    @Query(value = """
            select * from bank_account where id = :id FOR UPDATE
            """, nativeQuery = true)
    BankAccount findAndLockById(@Param("id") UUID id);

    @Query(value = """
            select * from bank_account where id in (:fromAccountId, :toAccountId)
            order by case when id = :fromAccountId then 0 else 1 end
            FOR UPDATE
            """, nativeQuery = true)
    List<BankAccount> findAndLockByIds(
            @Param("fromAccountId") UUID fromId,
            @Param("toAccountId") UUID toId
    );
}
