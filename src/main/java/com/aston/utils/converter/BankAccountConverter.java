package com.aston.utils.converter;

import com.aston.api.BankAccountData;
import com.aston.repository.BankAccount;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

import static com.aston.utils.MoneyConversionUtil.toMoneyBigDecimal;
import static com.aston.utils.MoneyConversionUtil.toMoneyLong;
import static com.aston.validation.BankAccountDataValidation.isNameValid;
import static com.aston.validation.BankAccountDataValidation.isPinCodeValid;

@Component
public class BankAccountConverter {

    public static BankAccount createBankAccountEntity(BankAccountData data) {
        isNameValid(data.getName());
        isPinCodeValid(data.getPinCode());
        return new BankAccount(
                data.getId() == null ? UUID.randomUUID() : data.getId(),
                data.getNumber() == null ? getRandomBankAccountNumber(10) : data.getNumber(),
                data.getName(),
                data.getPinCode(),
                data.getAmount() == null ? 0L : toMoneyLong(data.getAmount())
        );
    }

    private static String getRandomBankAccountNumber(Integer length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return sb.toString();
    }

    public static BankAccountData createBankAccountData(BankAccount entity) {
        return new BankAccountData(
                entity.getName(),
                entity.getNumber(),
                toMoneyBigDecimal(entity.getAmount())
        );
    }
}
