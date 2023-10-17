package com.aston.operation;

import com.aston.api.BankAccountData;
import com.aston.repository.BankAccount;
import com.aston.repository.BankAccountRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.aston.utils.converter.MoneyConverter.toMoneyLong;
import static com.aston.validation.BankAccountDataValidation.isAccountNumberValid;
import static com.aston.validation.BankAccountDataValidation.isAmountValid;

@Getter
@Setter
@NoArgsConstructor
@Component
public class DepositBankAccountOperation implements BankAccountOperation {

    private BankAccountRepository repository;

    @Autowired
    public DepositBankAccountOperation(BankAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public List<BankAccount> execute(BankAccountData data) {
        isAccountNumberValid(data.getNumber());
        isAmountValid(data.getCreditAmount());
        BankAccount account = repository.findAndLockByNumber(data.getNumber());
        account.setAmount(account.getAmount() + toMoneyLong(data.getCreditAmount()));
        return List.of(repository.save(account));
    }
}
