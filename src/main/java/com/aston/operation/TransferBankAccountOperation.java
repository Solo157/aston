package com.aston.operation;

import com.aston.api.BankAccountData;
import com.aston.repository.BankAccount;
import com.aston.repository.BankAccountRepository;
import com.aston.utils.converter.BankAccountConverter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.aston.validation.BankAccountDataValidation.*;
import java.util.List;

import static com.aston.utils.MoneyConversionUtil.toMoneyLong;

@NoArgsConstructor
@Component
public class TransferBankAccountOperation implements BankAccountOperation {

    private BankAccountRepository repository;

    @Autowired
    public TransferBankAccountOperation(BankAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public List<BankAccountData> execute(BankAccountData data) {
        isAmountValid(data.getDebitAmount());
        isPinCodeValid(data.getPinCode());
        isAccountNumberValid(data.getNumber());
        isToAccountNumberValid(data.getToAccountNumber());

        List<BankAccount> accounts = repository.findAndLockByNumbers(data.getNumber(), data.getToAccountNumber());
        checkAccountsOnCorrection(accounts, data);
        BankAccount fromAccount = accounts.get(0);
        BankAccount toAccount = accounts.get(1);

        fromAccount.setAmount(
                fromAccount.getAmount() - toMoneyLong(data.getDebitAmount())
        );
        toAccount.setAmount(
                toAccount.getAmount() + toMoneyLong(data.getDebitAmount())
        );
        return repository.saveAll(List.of(toAccount, fromAccount))
                .stream()
                .map(BankAccountConverter::createBankAccountData)
                .toList();
    }

    private void checkAccountsOnCorrection(List<BankAccount> accounts, BankAccountData data) {
        long accountFutureAmount = accounts.get(0).getAmount()-toMoneyLong(data.getDebitAmount());
        boolean pinCodesInEquality = !accounts.get(0).getPinCode().equals(data.getPinCode());
        if (accounts.size() != 2) throw new IllegalArgumentException();
        if (pinCodesInEquality) throw new IllegalArgumentException();
        if (accountFutureAmount < 0) throw new IllegalArgumentException();
    }
}
