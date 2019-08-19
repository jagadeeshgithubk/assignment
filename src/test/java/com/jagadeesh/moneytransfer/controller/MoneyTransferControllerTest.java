package com.jagadeesh.moneytransfer.controller;

import com.jagadeesh.moneytransfer.dto.MoneyTransferDTO;
import io.restassured.RestAssured;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.jagadeesh.moneytransfer.TestUtils;

import javax.ws.rs.core.Application;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class MoneyTransferControllerTest extends JerseyTest {


    @BeforeClass
    public static void configureRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 9998;
    }

    @Before
    public void initializeData() {
        TestUtils.initializeTestData();
    }

    @Test
    public void validTransactionShouldSucceed() {
        MoneyTransferDTO moneyTransfer = new MoneyTransferDTO("1", "2", "10");

        given().contentType("application/json").body(moneyTransfer)
                .when()
                    .post("/transferMoney")
                .then()
                    .statusCode(200)
                    .body("size()", is(2))
                    .body("[0].accountId", equalTo("1"))
                    .body("[0].balance", equalTo("90.10"))
                    .body("[1].accountId", equalTo("2"))
                    .body("[1].balance", equalTo("100.22"));
    }

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(MoneyTransferController.class);
    }
}