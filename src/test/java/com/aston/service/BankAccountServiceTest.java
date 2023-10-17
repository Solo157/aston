package com.aston.service;

import com.aston.api.BankAccountData;
import com.aston.repository.BankAccount;
import com.aston.repository.BankAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Transactional
@SpringBootTest
class BankAccountServiceTest {

    @Autowired
    private BankAccountService service;

    @Autowired
    private BankAccountRepository repository;

	@Test
    void depositAmountToBankAccount_manyThreadsIncreaseAmountInParallel_amountIsIncreasedSuccessfully () throws InterruptedException {
		UUID randomUUID = UUID.randomUUID();
		Long accountNumber = 1234567890L;
		int threadCount = 1000;
		Thread createBankAccountThread = new Thread(() -> { setBankAccount(randomUUID, accountNumber); });
		createBankAccountThread.start();
		createBankAccountThread.join();

		BankAccountData data = new BankAccountData();
		data.setNumber(accountNumber);
		data.setCreditAmount(BigDecimal.valueOf(1.00));
		List<Thread> threadList = new ArrayList<>();
		for (int i = 0; i < threadCount; i++) {
			Thread thread = new Thread(() -> { service.depositAmountToBankAccount(data); });
			threadList.add(thread);
			thread.start();
		}
		for (int i = 0; i < threadCount; i++) {
			threadList.get(i).join();
		}

		BankAccount account = repository.findAll().get(0);
		assertEquals(account.getAmount(), 100000);
		assertEquals(account.getNumber(), accountNumber);
    }

	@Transactional(propagation = REQUIRES_NEW)
	void setBankAccount(UUID randomUUID, Long accountNumber) {
		BankAccount entity = new BankAccount();
		entity.setId(randomUUID);
		entity.setNumber(accountNumber);
		entity.setName("Test Entity");
		entity.setAmount(0L);
		entity.setPinCode(1234);
		repository.save(entity);
	}
}
