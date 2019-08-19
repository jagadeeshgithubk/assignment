package com.jagadeesh.moneytransfer.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.jagadeesh.moneytransfer.exception.DuplicateAccountException;
import com.jagadeesh.moneytransfer.model.Account;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountDataStoreDaoTest {

    private AccountDataStoreDao accountDataStoreDao = AccountDataStoreDao.getInstance();

    @BeforeEach
    void cleanUp() {
        accountDataStoreDao.removeAll();
    }

    @Test
    @DisplayName("It's possible to add new account")
    void addingAccountShouldSucceed() {
        Account anyAccount = new Account();

        accountDataStoreDao.addAccount(anyAccount);

        assertEquals(1, accountDataStoreDao.getAll().size());
        assertEquals(anyAccount, accountDataStoreDao.getById(anyAccount.getAccountId()));
    }

    @Test
    @DisplayName("Adding duplicate account should throw DuplicateAccountException")
    void addingDuplicateShouldReturnAlreadyPersistedAccount() {
        String accountId = "1337";
        Account testAccount = new Account(accountId, "100000");

        accountDataStoreDao.addAccount(testAccount);

        Account accountWithDuplicatedId = new Account(accountId, "0");

        assertThrows(DuplicateAccountException.class, () -> accountDataStoreDao.addAccount(accountWithDuplicatedId));
        assertEquals(1, accountDataStoreDao.getAll().size());
    }

    @Test
    @DisplayName("It's possible to remove all accounts")
    void shouldBePossibleToRemoveAllAccounts() {
        int numberOfAccounts = 5;

        insertAccountsIntoRepository(numberOfAccounts);

        assertEquals(numberOfAccounts, accountDataStoreDao.getAll().size());

        accountDataStoreDao.removeAll();

        assertEquals(0, accountDataStoreDao.getAll().size());
    }

    @Test
    @DisplayName("It's possible to retrieve account from database")
    void shouldReturnExistingAccount() {
        final String existingId = "1";
        Account account = new Account(existingId, "10");

        accountDataStoreDao.addAccount(account);

        Account retrievedAccount = accountDataStoreDao.getById(existingId);

        assertEquals(account, retrievedAccount);
    }

    private void insertAccountsIntoRepository(int numberOfAccounts) {
        IntStream.range(0, numberOfAccounts).forEach(a -> accountDataStoreDao.addAccount(new Account()));
    }

}