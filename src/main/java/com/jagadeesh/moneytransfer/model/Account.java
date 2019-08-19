package com.jagadeesh.moneytransfer.model;

import com.jagadeesh.moneytransfer.exception.IllegalOperationException;
import com.jagadeesh.moneytransfer.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Account {

    private final String accountId;
    private BigDecimal balance;

    public Account() {
        this.accountId = UUID.randomUUID().toString();
        this.balance = BigDecimal.ZERO;
    }

    public Account(String balance) {
        this.accountId = UUID.randomUUID().toString();
        this.balance = new BigDecimal(balance);
    }

    public Account(String accountId, String balance) {
        this.accountId = accountId;
        this.balance = new BigDecimal(balance);
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal credit(BigDecimal amount) {
        validate(amount);

        balance = balance.add(amount);
        return balance;
    }

    public BigDecimal debit(BigDecimal amount) {
        validate(amount);

        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Debit can't be performed due to lack of funds on the account.");
        }

        balance = balance.subtract(amount);
        return balance;
    }

    private void validate(BigDecimal amount) {
        if (Objects.isNull(amount) || BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalOperationException("You can only issue positive amount.");
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountId.equals(account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}
