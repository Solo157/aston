package com.aston.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountData {
    @JsonInclude(NON_NULL) private UUID id;
    @JsonInclude(NON_NULL) private String number;
    private String name;
    private BigDecimal amount;
    @JsonInclude(NON_NULL) private BigDecimal creditAmount;
    @JsonInclude(NON_NULL) private BigDecimal debitAmount;
    @JsonInclude(NON_NULL) private Integer pinCode;
    @JsonInclude(NON_NULL) private String toAccountNumber;

    public BankAccountData(String name, String number, BigDecimal amount) {
        this.name = name;
        this.number = number;
        this.amount = amount;
    }
}
