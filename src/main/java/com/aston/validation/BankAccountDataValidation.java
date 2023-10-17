package com.aston.validation;

import com.aston.exception.NotValidRequestException;
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
            throw new NotValidRequestException("Name must be more then 1 and less then 200 and equal [a-zA-Z0-9]+");
        }
    }

    public static void isPinCodeValid(Integer pinCode) {
        if (pinCode.toString().length() != 4) {
            throw new NotValidRequestException("PinCode's length must be equal 4");
        }
    }

    public static void isAccountNumberValid(Long number) {
        if (number.toString().length() != 10) {
            throw new NotValidRequestException("Number's length must be equal 10");
        }
        if (!repository.existsBankAccountByNumber(number)) {
            throw new NotValidRequestException("Account is not found");
        }
    }

    public static void isToAccountNumberValid(Long number) {
        isAccountNumberValid(number);
    }

    public static void isAmountValid(BigDecimal amount) {
        if (amount == null || amount.compareTo(ZERO) < 0) {
            throw new NotValidRequestException("Amount is not valid");
        }
    }
}
