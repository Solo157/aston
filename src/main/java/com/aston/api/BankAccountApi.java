package com.aston.api;

import com.aston.service.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aston/accounts")
public class BankAccountApi {

    private final BankAccountService service;

    public BankAccountApi(BankAccountService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<BankAccountData> getBankAccounts() {
        return service.getBankAccounts();
    }

    @PostMapping("/create")
    public BankAccountData createBankAccount(@RequestBody BankAccountData data) {
        return service.createBankAccount(data);
    }

    @PatchMapping("/deposit")
    public BankAccountData depositAmountToBankAccount(@RequestBody BankAccountData data) {
        return service.depositAmountToBankAccount(data);
    }

    @PatchMapping("/withdraw")
    public BankAccountData withdrawToBankAccount(@RequestBody BankAccountData data) {
        return service.withdrawAmountFromBankAccount(data);
    }

    @PatchMapping("/transfer")
    public List<BankAccountData> transferAmountToAnotherBankAccount(@RequestBody BankAccountData data) {
        return service.transferAmountToAnotherBankAccount(data);
    }
}
