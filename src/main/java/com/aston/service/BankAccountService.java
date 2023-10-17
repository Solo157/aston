package com.aston.service;

import com.aston.api.BankAccountData;
import com.aston.operation.BankAccountOperationManager;
import com.aston.repository.BankAccountRepository;
import com.aston.utils.converter.BankAccountConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.aston.operation.BankAccountOperationManager.AccountOperation.*;

@Service
public class BankAccountService {

    private final BankAccountRepository repository;

    private final BankAccountOperationManager operationManager;

    public BankAccountService(BankAccountRepository repository, BankAccountOperationManager operationManager) {
        this.repository = repository;
        this.operationManager = operationManager;
    }

    public List<BankAccountData> getBankAccounts() {
        return repository.findAll().stream()
                .map(BankAccountConverter::createBankAccountData)
                .collect(Collectors.toList());
    }

    public BankAccountData createBankAccount(BankAccountData accountData) {
        return operationManager.getBankAccountOperation(CREATE).execute(accountData).get(0);
    }

    public BankAccountData depositAmountToBankAccount(BankAccountData accountData) {
        return operationManager.getBankAccountOperation(DEPOSIT).execute(accountData).get(0);
    }

    public BankAccountData withdrawAmountFromBankAccount(BankAccountData accountData) {
        return operationManager.getBankAccountOperation(WITHDRAW).execute(accountData).get(0);
    }

    public List<BankAccountData> transferAmountToAnotherBankAccount(BankAccountData accountData) {
        return operationManager.getBankAccountOperation(TRANSFER).execute(accountData);
    }
}
