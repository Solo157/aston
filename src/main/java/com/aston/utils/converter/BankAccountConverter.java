package com.aston.utils.converter;

import com.aston.api.BankAccountData;
import com.aston.repository.BankAccount;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.aston.utils.MoneyConversionUtil.toMoneyBigDecimal;
import static com.aston.utils.MoneyConversionUtil.toMoneyLong;

@Component
public class BankAccountConverter {

    public static BankAccount createBankAccountEntity(BankAccountData data) {
        BankAccount entity = new BankAccount();

        if (data.isNameValid() && data.isPinCodeValid()) {
            entity.setName(data.getName());
            entity.setPinCode(data.getPinCode());
        }
        if (data.isSumValid())
            entity.setAmount(toMoneyLong(data.getAmount()));
        else
            entity.setAmount(0L);

        entity.setId(UUID.randomUUID());

        return entity;
    }

    public static BankAccountData createBankAccountData(BankAccount entity) {
        BankAccountData data = new BankAccountData();
        if (entity.getId() != null) data.setId(entity.getId());
        if (entity.getName() != null) data.setName(entity.getName());
        if (entity.getPinCode() != null) data.setPinCode(entity.getPinCode());
        if (entity.getAmount() != null) data.setAmount(toMoneyBigDecimal(entity.getAmount()));
        return data;
    }
}
