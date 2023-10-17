package com.aston.service.operation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BankAccountOperationManager {

    private CreateBankAccountOperation createOperation;
    private DepositBankAccountOperation depositOperation;
    private WithdrawBankAccountOperation withdrawOperation;
    private TransferBankAccountOperation transferOperation;

    public BankAccountOperation getBankAccountOperation(AccountOperation operation) {
        return switch (operation) {
            case CREATE -> createOperation;
            case DEPOSIT -> depositOperation;
            case WITHDRAW -> withdrawOperation;
            case TRANSFER -> transferOperation;
        };
    }

    public enum AccountOperation {
        CREATE,
        DEPOSIT,
        WITHDRAW,
        TRANSFER;
    }
}
