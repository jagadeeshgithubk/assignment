package com.jagadeesh.moneytransfer.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InsufficientFundsException extends RuntimeException implements ExceptionMapper<InsufficientFundsException> {

    public InsufficientFundsException() {
        super("Insufficient account balance to perform this operation.");
    }

    public InsufficientFundsException(String message) {
        super(message);
    }

    @Override
    public Response toResponse(InsufficientFundsException exception) {
        return Response
                .status(Response.Status.CONFLICT)
                .entity(exception.getMessage())
                .type("text/plain")
                .build();
    }
}
