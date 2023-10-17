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

import static com.aston.utils.converter.BankAccountConverter.createBankAccountEntity;
import static com.aston.utils.validation.BankAccountDataValidation.*;

@Getter
@Setter
@NoArgsConstructor
@Component
public class CreateBankAccountOperation implements BankAccountOperation {

    private BankAccountRepository repository;

    @Autowired
    public CreateBankAccountOperation(BankAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public List<BankAccount> execute(BankAccountData data) {
        isNameValid(data.getName());
        isPinCodeValid(data.getPinCode());
        BankAccount account = repository.save(createBankAccountEntity(data));
        return List.of(account);
    }
}
