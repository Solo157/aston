package com.aston.service;

import com.aston.api.BankAccountData;
import com.aston.service.operation.BankAccountOperationManager;
import com.aston.repository.BankAccount;
import com.aston.repository.BankAccountRepository;
import com.aston.utils.converter.BankAccountConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.aston.utils.converter.BankAccountConverter.createBankAccountData;

import java.util.List;
import java.util.stream.Collectors;

import static com.aston.service.operation.BankAccountOperationManager.AccountOperation.*;

@Service
public class BankAccountService {

    private final BankAccountRepository repository;

    private final BankAccountOperationManager operationManager;

    public BankAccountService(BankAccountRepository repository, BankAccountOperationManager operationManager) {
        this.repository = repository;
        this.operationManager = operationManager;
    }

    @Transactional
    public List<BankAccountData> getBankAccounts() {
        return repository.findAll().stream()
                .map(BankAccountConverter::createBankAccountData)
                .collect(Collectors.toList());
    }

    public BankAccountData createBankAccount(BankAccountData accountData) {
        BankAccount account = operationManager.getBankAccountOperation(CREATE).execute(accountData).get(0);
        BankAccountData bankAccountData = createBankAccountData(account);
        bankAccountData.setNumber(account.getNumber());
        return bankAccountData;
    }

    public BankAccountData depositAmountToBankAccount(BankAccountData accountData) {
        return createBankAccountData(
                operationManager.getBankAccountOperation(DEPOSIT).execute(accountData).get(0)
        );
    }

    public BankAccountData withdrawAmountFromBankAccount(BankAccountData accountData) {
        return createBankAccountData(
                operationManager.getBankAccountOperation(WITHDRAW).execute(accountData).get(0)
        );
    }

    public List<BankAccountData> transferAmountToAnotherBankAccount(BankAccountData accountData) {
        return operationManager.getBankAccountOperation(TRANSFER).execute(accountData)
                .stream()
                .map(BankAccountConverter::createBankAccountData)
                .toList();
    }
}
