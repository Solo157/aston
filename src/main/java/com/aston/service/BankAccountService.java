package com.aston.service;

import com.aston.api.BankAccountData;
import com.aston.exception.NotValidRequestException;
import com.aston.repository.BankAccount;
import com.aston.repository.BankAccountRepository;
import com.aston.utils.converter.BankAccountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.aston.utils.MoneyConversionUtil.toMoneyLong;
import static com.aston.utils.converter.BankAccountConverter.createBankAccountData;
import static com.aston.utils.converter.BankAccountConverter.createBankAccountEntity;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository accountRepository;

    public List<BankAccountData> getBankAccounts() {
        return accountRepository.findAll().stream()
                .map(BankAccountConverter::createBankAccountData)
                .collect(Collectors.toList());
    }

    @Transactional
    public BankAccountData createBankAccount(BankAccountData accountData) {
        if (!accountData.isNameValid()) throw new NotValidRequestException("error.request.name.notValid");
        if (!accountData.isPinCodeValid()) throw new NotValidRequestException("error.request.pinCode.notValid");

        BankAccount entity = accountRepository.save(
                createBankAccountEntity(accountData)
        );

        return createBankAccountData(entity);
    }

    @Transactional
    public BankAccountData addSumToBankAccount(BankAccountData accountData) {
        if (!accountData.isSumValid()) throw new NotValidRequestException("error.request.sum.notValid");
        if (accountData.getId() == null) throw new NullPointerException("Отсутствует id в запросе");

        BankAccount entity = accountRepository.findAndLockById(accountData.getId());
        entity.setAmount(
                entity.getAmount() + toMoneyLong(accountData.getAmount())
        );
        return createBankAccountData(accountRepository.save(entity));
    }

    @Transactional
    public BankAccountData withdrawAmountFromBankAccount(BankAccountData accountData) {
        if (!accountData.isSumValid()) throw new NotValidRequestException("error.request.sum.notValid");
        if (!accountData.isPinCodeValid()) throw new NotValidRequestException("error.request.pinCode.notValid");
        if (accountData.getId() == null) throw new NullPointerException("Отсутствует id в запросе");

        //проверка пин-кода

        BankAccount entity = accountRepository.findAndLockById(accountData.getId());
        long withdrawAmount = entity.getAmount() - toMoneyLong(accountData.getAmount());
        if (withdrawAmount >= 0)
            entity.setAmount(withdrawAmount);
        else
            throw new RuntimeException("error.withdraw.amount.negative");
        return createBankAccountData(accountRepository.save(entity));
    }

    public List<BankAccountData> transferAmountToAnotherBankAccount(BankAccountData accountData) {
        if (!accountData.isSumValid()) throw new NotValidRequestException("error.request.sum.notValid");
        if (!accountData.isPinCodeValid()) throw new NotValidRequestException("error.request.pinCode.notValid");
        if (accountData.getId() == null) throw new NullPointerException("Отсутствует id в запросе");
        if (accountData.getToAccountId() == null) throw new NullPointerException("Отсутствует id в запросе");

        List<BankAccount> accountEntities = accountRepository.findAndLockByIds(accountData.getId(), accountData.getToAccountId());
        BankAccount fromAccount = accountEntities.get(0);
        BankAccount toAccount = accountEntities.get(1);
        long transferAmount = toMoneyLong(accountData.getAmount());
        long fromAccountBalance = fromAccount.getAmount() - transferAmount;
        long toAccountBalance = toAccount.getAmount() + transferAmount;
        if (accountEntities.size() < 2) {
            throw new NullPointerException("Один или более аккаунтов не найдены");
        } else if (!fromAccount.getPinCode().equals(accountData.getPinCode())) {
            throw new RuntimeException("error.request.pinCode.wrong");
        } else if (fromAccountBalance < 0) {
            throw new RuntimeException("error.withdraw.amount.negative");
        }

        fromAccount.setAmount(fromAccountBalance);
        toAccount.setAmount(toAccountBalance);
        return accountRepository.saveAll(List.of(toAccount, fromAccount))
                        .stream()
                        .map(BankAccountConverter::createBankAccountData)
                        .toList();
    }
}
