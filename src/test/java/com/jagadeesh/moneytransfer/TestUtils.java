package com.jagadeesh.moneytransfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jagadeesh.moneytransfer.dao.AccountDataStoreDao;
import com.jagadeesh.moneytransfer.model.Account;

public class TestUtils {

    private static final Logger log = LoggerFactory.getLogger(TestUtils.class);

    private TestUtils() {
    }

    public static void initializeTestData() {

        AccountDataStoreDao repository = AccountDataStoreDao.getInstance();

        repository.removeAll();

        repository.addAccount(new Account("1", "100.10"));
        repository.addAccount(new Account("2", "90.22"));
        repository.addAccount(new Account("3", "20.22"));

        log.info("System has been initialized with test data.");
    }
}
