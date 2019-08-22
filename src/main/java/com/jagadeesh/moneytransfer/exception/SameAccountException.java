package com.jagadeesh.moneytransfer.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SameAccountException extends RuntimeException implements ExceptionMapper<SameAccountException> {

    public SameAccountException() {
        super("Cannot transfer from account to itself.");
    }

    public SameAccountException(String message) {
        super(message);
    }

    @Override
    public Response toResponse(SameAccountException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .type("text/plain")
                .build();
    }
}
