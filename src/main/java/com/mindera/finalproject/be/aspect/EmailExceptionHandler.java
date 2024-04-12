package com.mindera.finalproject.be.aspect;


import com.mindera.finalproject.be.exception.email.EmailException;
import com.mindera.finalproject.be.exception.email.EmailGetTemplateException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import java.util.Date;

import static com.mindera.finalproject.be.messages.Messages.UNEXPECTED_ERROR;

public class EmailExceptionHandler implements ExceptionMapper<EmailException> {
    @Override
    public Response toResponse(EmailException e) {
        if (e instanceof EmailGetTemplateException) {
            Error error = new Error.Builder()
                    .message(e.getMessage())
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .timestamp(new Date())
                    .build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(UNEXPECTED_ERROR).build();
    }
}
