package com.aston.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

import static com.aston.api.BankAccountData.BankAccountDataConstant.*;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
public class BankAccountData {
    @JsonInclude(NON_NULL)
    private UUID id;
    private String name;
    private BigDecimal amount;

    @JsonProperty("pin_code")
    @JsonInclude(NON_NULL)
    private Integer pinCode;

    @JsonProperty("to_account_id")
    @JsonInclude(NON_NULL)
    private UUID toAccountId;

    @JsonIgnore
    public Boolean isNameValid() {
        return name.length() >= NAME_MIN_LENGTH
                && name.length() <= NAME_MAX_LENGTH
                && name.matches(NAME_REGEX);
    }

    @JsonIgnore
    public Boolean isPinCodeValid() {
        return pinCode.toString().length() == PINCODE_LENGTH;
    }

    @JsonIgnore
    public Boolean isSumValid() {
        return amount != null && amount.compareTo(MIN_SUM) > 0;
    }

    public static class BankAccountDataConstant {
        public static final Integer NAME_MIN_LENGTH = 1;
        public static final Integer NAME_MAX_LENGTH = 200;
        public static final String NAME_REGEX = "[a-zA-Z0-9]+";
        public static final Integer PINCODE_LENGTH = 4;
        public static final BigDecimal MIN_SUM = new BigDecimal("0.0");
    }
}
