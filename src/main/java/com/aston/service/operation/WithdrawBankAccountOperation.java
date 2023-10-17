package com.aston.service.operation;

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
import static com.aston.utils.validation.BankAccountDataValidation.*;

@Getter
@Setter
@NoArgsConstructor
@Component
public class WithdrawBankAccountOperation implements BankAccountOperation {

    private BankAccountRepository repository;

    @Autowired
    public WithdrawBankAccountOperation(BankAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public List<BankAccount> execute(BankAccountData data) {
        isAmountValid(data.getDebitAmount());
        isPinCodeValid(data.getPinCode());
        isAccountNumberValid(data.getNumber());

        BankAccount account = repository.findAndLockByNumber(data.getNumber());
        checkAccountsOnCorrection(account, data);
        account.setAmount(account.getAmount() - toMoneyLong(data.getDebitAmount()));

        return List.of(repository.save(account));
    }

    private void checkAccountsOnCorrection(BankAccount account, BankAccountData data) {
        if (account == null) throw new NullPointerException("Account is null");
        long accountFutureAmount = account.getAmount() - toMoneyLong(data.getDebitAmount());
        boolean pinCodesInEquality = !account.getPinCode().equals(data.getPinCode());
        if (pinCodesInEquality) throw new IllegalStateException("Pin code is wrong");
        if (accountFutureAmount < 0) throw new IllegalStateException("Withdraw's amount is bigger then account amount");
    }
}
