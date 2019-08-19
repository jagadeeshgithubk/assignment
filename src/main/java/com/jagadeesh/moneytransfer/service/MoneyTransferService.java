package com.jagadeesh.moneytransfer.service;

import com.jagadeesh.moneytransfer.dao.AccountDataStoreDao;
import com.jagadeesh.moneytransfer.dto.AccountDTO;
import com.jagadeesh.moneytransfer.dto.MoneyTransferDTO;
import com.jagadeesh.moneytransfer.exception.IllegalOperationException;
import com.jagadeesh.moneytransfer.exception.InsufficientFundsException;
import com.jagadeesh.moneytransfer.model.Account;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MoneyTransferService {

    //tieLock used to prevent deadlock (in a rare case when both accounts has the same hashcode).
    private static final Object tieLock = new Object();
    private static final MoneyTransferService INSTANCE = new MoneyTransferService(AccountDataStoreDao.getInstance());

    private final AccountDataStoreDao repository;

    private MoneyTransferService(AccountDataStoreDao repository) {
        this.repository = repository;
    }

    public static MoneyTransferService getInstance() {
        return INSTANCE;
    }

    public List<AccountDTO> transfer(final MoneyTransferDTO trx) {
        Account source = repository.getById(trx.getSource());
        Account target = repository.getById(trx.getTarget());

        if (source == null || target == null) {
            throw new IllegalOperationException("Account(s) doesn't exist. | Source: " + source + ", Target: " + target);
        }

        return transferMoney(source, target, trx.getAmount());
    }

    private List<AccountDTO> transferMoney(final Account sourceAccount,
            final Account targetAccount,
            final BigDecimal amount) {
        class TransferExecutor {
            private List<AccountDTO> execute() {
                if (sourceAccount.getBalance().compareTo(amount) < 0) {
                    throw new InsufficientFundsException("Money Transfer can't be performed due to lack of funds on the account.");
                }

                sourceAccount.debit(amount);
                targetAccount.credit(amount);

                return Collections.unmodifiableList(Arrays.asList(AccountDTO.from(sourceAccount), AccountDTO.from(targetAccount)));
            }
        }

        int sourceHash = System.identityHashCode(sourceAccount);
        int targetHash = System.identityHashCode(targetAccount);

        if (sourceHash < targetHash) {
            synchronized (sourceAccount) {
                synchronized (targetAccount) {
                    return new TransferExecutor().execute();
                }
            }
        } else if (sourceHash > targetHash) {
            synchronized (targetAccount) {
                synchronized (sourceAccount) {
                    return new TransferExecutor().execute();
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (sourceAccount) {
                    synchronized (targetAccount) {
                        return new TransferExecutor().execute();
                    }
                }
            }
        }
    }
}
