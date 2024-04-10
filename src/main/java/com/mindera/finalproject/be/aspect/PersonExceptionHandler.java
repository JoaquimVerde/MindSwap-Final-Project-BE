package com.mindera.finalproject.be.aspect;

import com.mindera.finalproject.be.exception.student.PersonAlreadyExistsException;
import com.mindera.finalproject.be.exception.student.PersonException;
import com.mindera.finalproject.be.exception.student.PersonNotATeacherException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Date;

import static com.mindera.finalproject.be.messages.Messages.UNEXPECTED_ERROR;

@Provider
public class PersonExceptionHandler implements ExceptionMapper<PersonException> {
    @Override
    public Response toResponse(PersonException e) {
        if (e instanceof PersonNotFoundException) {
            Error error = new Error.Builder()
                    .message(e.getMessage())
                    .status(Response.Status.NOT_FOUND.getStatusCode())
                    .timestamp(new Date())
                    .build();
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        if (e instanceof PersonAlreadyExistsException) {
            Error error = new Error.Builder()
                    .message(e.getMessage())
                    .status(Response.Status.CONFLICT.getStatusCode())
                    .timestamp(new Date())
                    .build();
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }
        if (e instanceof PersonNotATeacherException) {
            Error error = new Error.Builder()
                    .message(e.getMessage())
                    .status(Response.Status.BAD_REQUEST.getStatusCode())
                    .timestamp(new Date())
                    .build();
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(UNEXPECTED_ERROR).build();
    }
}
