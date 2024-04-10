package com.mindera.finalproject.be.aspect;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Date;

@Provider
public class ValidationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException e) {
        Error error = new Error.Builder()
                .message(e.getMessage())
                .status(Response.Status.BAD_REQUEST.getStatusCode())
                .timestamp(new Date())
                .build();
        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }
}
