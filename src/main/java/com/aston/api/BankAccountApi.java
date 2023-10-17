package com.aston.api;

import com.aston.service.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/aston/accounts")
public class BankAccountApi {

    private final BankAccountService service;

    public BankAccountApi(BankAccountService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<BankAccountData> getBankAccounts() { return service.getBankAccounts(); }

    @PostMapping("/create")
    public BankAccountData createBankAccount(@RequestBody BankAccountData data) {
        return service.createBankAccount(data);
    }

    @PostMapping("/{accountNumber}/deposit")
    public BankAccountData depositAmountToBankAccount(@PathVariable UUID accountNumber,@RequestParam BigDecimal amount) {
        BankAccountData accountData = new BankAccountData();
        accountData.setId(accountNumber);
        accountData.setAmount(amount);
        return service.addSumToBankAccount(accountData);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public BankAccountData withdrawToBankAccount(
            @PathVariable UUID accountNumber,
            @RequestParam Integer pinCode,
            @RequestParam BigDecimal amount
    ) {
        BankAccountData accountData = new BankAccountData();
        accountData.setId(accountNumber);
        accountData.setPinCode(pinCode);
        accountData.setAmount(amount);
        return service.withdrawAmountFromBankAccount(accountData);
    }

    @PostMapping("/{accountNumber}/transfer")
    public List<BankAccountData> transferAmountToAnotherBankAccount(
            @PathVariable(value = "accountNumber") UUID accountId,
            @RequestParam Integer pinCode,
            @RequestParam BigDecimal amount,
            @RequestParam(value = "toAccountNumber") UUID toAccountId
    ) {
        BankAccountData accountData = new BankAccountData();
        accountData.setId(accountId);
        accountData.setPinCode(pinCode);
        accountData.setAmount(amount);
        accountData.setToAccountId(toAccountId);
        return service.transferAmountToAnotherBankAccount(accountData);
    }
}
