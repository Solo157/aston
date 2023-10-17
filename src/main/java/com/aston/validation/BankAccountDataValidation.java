package com.aston.validation;

import com.aston.repository.BankAccount;
import com.aston.repository.BankAccountRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import static java.math.BigDecimal.ZERO;

@Getter
@Setter
@NoArgsConstructor
@Component
public class BankAccountDataValidation {

    private static BankAccountRepository repository;

    @Autowired
    public BankAccountDataValidation(BankAccountRepository accountRepository) {
        repository = accountRepository;
    }

    public static void isNameValid(String name) {
        if (name.length() < 1
                || name.length() > 200
                || !name.matches("[a-zA-Z0-9]+")) {
            throw new IllegalArgumentException();
        }
    }

    public static void isPinCodeValid(Integer pinCode) {
        if (pinCode.toString().length() != 4) {
            throw new IllegalArgumentException();
        }
    }

    public static void isAccountNumberValid(String number) {
        if (number.length() != 10) {
            throw new IllegalArgumentException();
        }
        BankAccount account = repository.findByNumber(number);
        if (account == null) {
            throw new IllegalArgumentException();
        }
    }

    public static void isToAccountNumberValid(String number) {
        isAccountNumberValid(number);
    }

    public static void isAmountValid(BigDecimal amount) {
        if (amount == null || amount.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }
}
