package com.aston;

import com.aston.api.BankAccountData;
import com.aston.repository.BankAccount;
import com.aston.repository.BankAccountRepository;
import com.aston.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
class AstonApplicationTests {

    @Autowired
    private BankAccountService service;

    @Autowired
    private BankAccountRepository repository;

//	@Autowired
//	private MockMvc mockMvc;

    @Test
	@Transactional
    void contextLoads() throws InterruptedException {
		UUID randomUUID = UUID.randomUUID();
		new Thread(() -> {
			setBank(randomUUID);
		}).start();

		Thread.sleep(1000);

		Integer threadCount = 100;

		// Создайте и запустите несколько потоков, которые будут пытаться заблокировать запись
		BankAccountData data = new BankAccountData();
		data.setId(randomUUID);
		data.setName("kil");
		data.setPinCode(1234);
		data.setAmount(BigDecimal.valueOf(1.00));

		List<Thread> threadList = new ArrayList<>();
		List<BigDecimal> sumList = new ArrayList<>();

		for (int i = 0; i < threadCount; i++) {
			Thread thread = new Thread(() -> {
				BankAccountData bankAccountData = service.depositAmountToBankAccount(data).get(0);
				sumList.add(bankAccountData.getAmount());
			});

			threadList.add(thread);
			thread.start();
		}

		for (int i = 0; i < threadCount; i++) {
			threadList.get(i).join();
			System.out.println(threadList.get(i).getState() + "**" + sumList.get(i));
		}



        System.out.println(repository.findAll().get(0).getAmount() + "!!!" + sumList.size());
    }

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void setBank(UUID randomUUID) {
		BankAccount entity = new BankAccount();
		entity.setId(randomUUID);
		entity.setName("Test Entity");
		entity.setAmount(0L);
		entity.setPinCode(1234);
		repository.save(entity);
	}
}
