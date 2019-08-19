package com.jagadeesh.moneytransfer.dao;

import com.jagadeesh.moneytransfer.exception.DuplicateAccountException;
import com.jagadeesh.moneytransfer.model.Account;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountDataStoreDao {

    private static final AccountDataStoreDao INSTANCE = new AccountDataStoreDao(new ConcurrentHashMap<>());
    private final Map<String, Account> accounts;

    private AccountDataStoreDao(Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    public static AccountDataStoreDao getInstance() {
        return INSTANCE;
    }

    public Account getById(String accountId) {
        return accounts.get(accountId);
    }

    public Collection<Account> getAll() {
        return accounts.values();
    }

    public Account addAccount(Account account) {
        Account accountExists = accounts.putIfAbsent(account.getAccountId(), account);
        if (accountExists != null) {
            throw new DuplicateAccountException(accountExists.getAccountId());
        }

        return getById(account.getAccountId());
    }

    public void removeAll() {
        synchronized (accounts) {
            accounts.clear();
        }
    }
}
