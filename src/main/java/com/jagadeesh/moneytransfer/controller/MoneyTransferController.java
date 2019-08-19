package com.jagadeesh.moneytransfer.controller;

import com.jagadeesh.moneytransfer.dto.AccountDTO;
import com.jagadeesh.moneytransfer.dto.MoneyTransferDTO;
import com.jagadeesh.moneytransfer.service.MoneyTransferService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/transferMoney")
public class MoneyTransferController {

    private final MoneyTransferService moneyTransferService = MoneyTransferService.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response submitMoneyTransfer(MoneyTransferDTO trx) {
        List<AccountDTO> result = moneyTransferService.transfer(trx);
        return Response.ok().entity(result).build();
    }

}
