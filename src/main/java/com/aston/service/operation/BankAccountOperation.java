package com.aston.service.operation;

import com.aston.api.BankAccountData;
import com.aston.repository.BankAccount;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface BankAccountOperation {
    List<BankAccount> execute(@NotNull BankAccountData data);
}
