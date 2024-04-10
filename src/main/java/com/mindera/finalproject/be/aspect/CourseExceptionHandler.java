package com.mindera.finalproject.be.aspect;

import com.mindera.finalproject.be.exception.course.CourseAlreadyExistsException;
import com.mindera.finalproject.be.exception.course.CourseException;
import com.mindera.finalproject.be.exception.course.CourseNotFoundException;
import com.mindera.finalproject.be.exception.course.MaxNumberOfStudentsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Date;

import static com.mindera.finalproject.be.messages.Messages.UNEXPECTED_ERROR;

@Provider
public class CourseExceptionHandler implements ExceptionMapper<CourseException> {
    @Override
    public Response toResponse(CourseException e) {
        if (e instanceof CourseNotFoundException) {
            Error error = new Error.Builder()
                    .message(e.getMessage())
                    .status(Response.Status.NOT_FOUND.getStatusCode())
                    .timestamp(new Date())
                    .build();
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        if (e instanceof CourseAlreadyExistsException) {
            Error error = new Error.Builder()
                    .message(e.getMessage())
                    .status(Response.Status.CONFLICT.getStatusCode())
                    .timestamp(new Date())
                    .build();
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }
        if (e instanceof MaxNumberOfStudentsException) {
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
