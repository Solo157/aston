package com.aston.operation;

import com.aston.api.BankAccountData;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface BankAccountOperation {
    List<BankAccountData> execute(@NotNull BankAccountData data);
}
