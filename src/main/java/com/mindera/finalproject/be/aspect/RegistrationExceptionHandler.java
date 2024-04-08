package com.mindera.finalproject.be.aspect;

import com.mindera.finalproject.be.exception.registration.RegistrationAlreadyExistsException;
import com.mindera.finalproject.be.exception.registration.RegistrationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Date;

import static com.mindera.finalproject.be.messages.Messages.UNEXPECTED_ERROR;

@Provider
public class RegistrationExceptionHandler implements ExceptionMapper<RegistrationException> {
    @Override
    public Response toResponse(RegistrationException e) {

        if (e instanceof RegistrationAlreadyExistsException) {
            Error error = new Error.Builder()
                    .message(e.getMessage())
                    .status(Response.Status.CONFLICT.getStatusCode())
                    .timestamp(new Date())
                    .build();
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(UNEXPECTED_ERROR).build();
    }
}
