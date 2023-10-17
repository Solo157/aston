package com.aston.utils.converter;

import com.aston.api.BankAccountData;
import com.aston.repository.BankAccount;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.aston.utils.converter.MoneyConverter.toMoneyBigDecimal;
import static com.aston.utils.converter.MoneyConverter.toMoneyLong;
import static com.aston.validation.BankAccountDataValidation.isNameValid;
import static com.aston.validation.BankAccountDataValidation.isPinCodeValid;

@Component
public class BankAccountConverter {

    public static BankAccount createBankAccountEntity(BankAccountData data) {
        isNameValid(data.getName());
        isPinCodeValid(data.getPinCode());
        return new BankAccount(
                data.getId(),
                data.getNumber() == null ? getRandomBankAccountNumber() : data.getNumber(),
                data.getName(),
                data.getPinCode(),
                data.getAmount() == null ? 0L : toMoneyLong(data.getAmount())
        );
    }

    private static Long getRandomBankAccountNumber() {
        return (long) (Math.floor(Math.random() * 9000000000L) + 1000000000);
    }

    public static BankAccountData createBankAccountData(BankAccount entity) {
        return new BankAccountData(
                entity.getName(),
                toMoneyBigDecimal(entity.getAmount())
        );
    }
}
