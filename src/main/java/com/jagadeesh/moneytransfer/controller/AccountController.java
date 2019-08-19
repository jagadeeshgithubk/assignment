package com.jagadeesh.moneytransfer.controller;

import com.jagadeesh.moneytransfer.model.Account;
import com.jagadeesh.moneytransfer.dao.AccountDataStoreDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;


@Path("/accounts")
public class AccountController {

    private final AccountDataStoreDao repository = AccountDataStoreDao.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAccounts() {
        return Response.ok(Collections.unmodifiableCollection(repository.getAll())).build();
    }

    @GET
    @Path("{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountById(@PathParam("accountId") String accountId) {
        Account account = repository.getById(accountId);
        if (account == null)
            return Response.noContent().build();

        return Response.ok(account).build();
    }

    @POST
    @Path("/addAccount")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewAccount(Account account) {
        repository.addAccount(account);
        return Response.ok(account).build();
    }


}
