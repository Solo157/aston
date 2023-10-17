package com.aston.api;

import com.aston.repository.BankAccount;
import com.aston.repository.BankAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.aston.utils.converter.BankAccountConverter.createBankAccountEntity;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class BankAccountApiTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private BankAccountRepository repository;

    @Test
    void getBankAccounts_successfully() throws Exception {
        BankAccountData accountData1 = new BankAccountData("name1", 1111);
        BankAccountData accountData2 = new BankAccountData("name2", 2222);
        repository.saveAll(List.of(createBankAccountEntity(accountData1), createBankAccountEntity(accountData2)));

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/aston/accounts/all")
                .contentType(APPLICATION_JSON_VALUE));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name", is("name1")))
                .andExpect(jsonPath("$.[0].amount", is(0.00)))
                .andExpect(jsonPath("$.[1].name", is("name2")))
                .andExpect(jsonPath("$.[1].amount", is(0.00)));
    }

    @Test
    void createBankAccount_successfully() throws Exception {
        BankAccountData accountData = new BankAccountData();
        accountData.setName("name1");
        accountData.setPinCode(1234);
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/aston/accounts/create")
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(accountData)));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.amount", is(0.00)));
    }

    @Test
    void depositAmountToBankAccount_successfully() throws Exception {
        BankAccount account = repository.save(createBankAccountEntity(new BankAccountData("name1", 1111)));

        BankAccountData accountData = new BankAccountData();
        accountData.setNumber(account.getNumber());
        accountData.setCreditAmount(BigDecimal.valueOf(1.01));
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .patch("/api/aston/accounts/deposit")
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(accountData)));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.amount", is(1.01)));
    }

    @Test
    void withdrawToBankAccount_successfully() throws Exception {
        BankAccount newAccount = createBankAccountEntity(new BankAccountData("name1", 1111));
        newAccount.setAmount(1000L);
        BankAccount account = repository.save(newAccount);

        BankAccountData accountData = new BankAccountData();
        accountData.setNumber(account.getNumber());
        accountData.setPinCode(account.getPinCode());
        accountData.setDebitAmount(BigDecimal.valueOf(5.00));
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .patch("/api/aston/accounts/withdraw")
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(accountData)));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.amount", is(5.00)));
    }

    @Test
    void transferAmountToAnotherBankAccount_successfully() throws Exception {
        BankAccount account1 = createBankAccountEntity(new BankAccountData("name1", 1111));
        account1.setAmount(1000L);
        BankAccount account2 = createBankAccountEntity(new BankAccountData("name2", 2222));
        repository.saveAll(List.of(account1, account2));

        BankAccountData accountData = new BankAccountData();
        accountData.setNumber(account1.getNumber());
        accountData.setPinCode(account1.getPinCode());
        accountData.setDebitAmount(BigDecimal.valueOf(5.00));
        accountData.setToAccountNumber(account2.getNumber());
        ObjectMapper mapper = new ObjectMapper();

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .patch("/api/aston/accounts/transfer")
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(accountData)));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name", is("name1")))
                .andExpect(jsonPath("$.[0].amount", is(5.00)))
                .andExpect(jsonPath("$.[1].name", is("name2")))
                .andExpect(jsonPath("$.[1].amount", is(5.00)));
    }
}
