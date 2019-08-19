package com.jagadeesh.moneytransfer.service;

import com.jagadeesh.moneytransfer.dto.MoneyTransferDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jagadeesh.moneytransfer.dao.AccountDataStoreDao;
import com.jagadeesh.moneytransfer.dto.AccountDTO;
import com.jagadeesh.moneytransfer.exception.IllegalOperationException;
import com.jagadeesh.moneytransfer.exception.InsufficientFundsException;
import com.jagadeesh.moneytransfer.model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTransferServiceTest {

    private final static Logger log = LoggerFactory.getLogger(MoneyTransferServiceTest.class);

    private static final String ACCOUNT_ID_1 = "1";
    private static final String ACCOUNT_ID_2 = "2";

    private final AccountDataStoreDao repository = AccountDataStoreDao.getInstance();
    private final MoneyTransferService moneyTransferService = MoneyTransferService.getInstance();

    private Account accountA;
    private Account accountB;

    @BeforeEach
    void setUp() {
        repository.removeAll();

        accountA = new Account(ACCOUNT_ID_1, "100.12");
        accountB = new Account(ACCOUNT_ID_2, "99.23");

        repository.addAccount(accountA);
        repository.addAccount(accountB);
    }

    @Test
    @DisplayName("Successful money transfer test")
    void successfulTransaction() {
        MoneyTransferDTO trx = new MoneyTransferDTO(ACCOUNT_ID_1, ACCOUNT_ID_2, "10.00");

        moneyTransferService.transfer(trx);

        assertEquals(new BigDecimal("90.12"), repository.getById(accountA.getAccountId()).getBalance());
        assertEquals(new BigDecimal("109.23"), repository.getById(accountB.getAccountId()).getBalance());
    }

    @Test
    @DisplayName("Successful concurrent money transfers - two threads")
    void simpleConcurrentSuccessfulTransactions() throws ExecutionException, InterruptedException {
        CompletableFuture moneyTransaction = CompletableFuture.runAsync(() -> {
            MoneyTransferDTO trx = new MoneyTransferDTO(ACCOUNT_ID_1, ACCOUNT_ID_2, "10.00");
            List<AccountDTO> transfer = moneyTransferService.transfer(trx);
            log.info("[Thread-" + Thread.currentThread().getId() + "] Result: " + transfer);
        });

        CompletableFuture reverseMoneyTransaction = CompletableFuture.runAsync(() -> {
            MoneyTransferDTO oppositeTrx = new MoneyTransferDTO(ACCOUNT_ID_2, ACCOUNT_ID_1, "20.00");
            List<AccountDTO> transfer = moneyTransferService.transfer(oppositeTrx);
            log.info("[Thread-" + Thread.currentThread().getId() + "] Result: " + transfer);
        });

        CompletableFuture.allOf(moneyTransaction, reverseMoneyTransaction).get();

        assertEquals(new BigDecimal("110.12"), repository.getById(ACCOUNT_ID_1).getBalance());
        assertEquals(new BigDecimal("89.23"), repository.getById(ACCOUNT_ID_2).getBalance());
    }

    @Test
    @DisplayName("Successful concurrent money transfers - two hundred threads")
    void concurrentSuccessfulTransactions() throws InterruptedException {

        Runnable transferFromOneToTwo = () -> {
            MoneyTransferDTO trx = new MoneyTransferDTO(ACCOUNT_ID_1, ACCOUNT_ID_2, "0.6");
            List<AccountDTO> transfer = moneyTransferService.transfer(trx);
            log.info("[Thread-" + Thread.currentThread().getId() + "] Result: " + transfer);
        };

        Runnable transferFromTwoToOne = () -> {
            MoneyTransferDTO oppositeTrx = new MoneyTransferDTO(ACCOUNT_ID_2, ACCOUNT_ID_1, "0.3");
            List<AccountDTO> transfer = moneyTransferService.transfer(oppositeTrx);
            log.info("[Thread-" + Thread.currentThread().getId() + "] Result: " + transfer);
        };

        ExecutorService executorService = Executors.newCachedThreadPool();

        for(int i=0; i<100; i++) {
            executorService.submit(transferFromOneToTwo);
            executorService.submit(transferFromTwoToOne);
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(new BigDecimal("70.12"), repository.getById(ACCOUNT_ID_1).getBalance());
        assertEquals(new BigDecimal("129.23"), repository.getById(ACCOUNT_ID_2).getBalance());
    }

    @Test
    @DisplayName("Unsuccessful money transfer (insufficient balance)")
    void insufficientBalanceTest() {
        final String lowBalanceAccountId = "3";
        Account lowBalanceAccount = new Account(lowBalanceAccountId, "0.12");
        repository.addAccount(lowBalanceAccount);

        MoneyTransferDTO trx = new MoneyTransferDTO(lowBalanceAccountId, ACCOUNT_ID_2, "10.00");

        assertThrows(InsufficientFundsException.class, () -> moneyTransferService.transfer(trx));

        assertEquals(new BigDecimal("0.12"), repository.getById(lowBalanceAccount.getAccountId()).getBalance());
        assertEquals(new BigDecimal("99.23"), repository.getById(ACCOUNT_ID_2).getBalance());
    }

    @Test
    @DisplayName("If source account's doesnt exist (is null) throw IllegalOperationException")
    void nonExistingSourceAccountThrowsException() {
        final String nonExistingAccount = "999";
        MoneyTransferDTO trx = new MoneyTransferDTO(nonExistingAccount, ACCOUNT_ID_2, "10.00");

        assertThrows(IllegalOperationException.class, () -> moneyTransferService.transfer(trx));
    }

    @Test
    @DisplayName("If target account's doesnt exist (is null) throw IllegalOperationException")
    void nonExistingTargetAccountThrowsException() {
        final String nonExistingAccount = "999";
        MoneyTransferDTO trx = new MoneyTransferDTO(ACCOUNT_ID_1, nonExistingAccount, "10.00");

        assertThrows(IllegalOperationException.class, () -> moneyTransferService.transfer(trx));
    }
}