package com.jagadeesh.moneytransfer.dto;

import com.jagadeesh.moneytransfer.model.Account;

public class AccountDTO {

    private final String accountId;
    private final String balance;

    private AccountDTO(String accountId, String balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public static AccountDTO from(Account account) {
        return new AccountDTO(account.getAccountId(), String.valueOf(account.getBalance()));
    }

    public String getAccountId() {
        return accountId;
    }

    public String getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "accountId='" + accountId + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}