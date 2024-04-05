package com.mindera.finalproject.be.aspect;

import com.mindera.finalproject.be.exception.project.ProjectAlreadyExistsException;
import com.mindera.finalproject.be.exception.project.ProjectException;
import com.mindera.finalproject.be.exception.project.ProjectNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Date;

import static com.mindera.finalproject.be.messages.Messages.UNEXPECTED_ERROR;

@Provider
public class ProjectExceptionHandler implements ExceptionMapper<ProjectException> {
    @Override
    public Response toResponse(ProjectException e) {
        if (e instanceof ProjectNotFoundException) {
            Error error = new Error.Builder()
                    .message(e.getMessage())
                    .status(Response.Status.NOT_FOUND.getStatusCode())
                    .timestamp(new Date())
                    .build();
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        if (e instanceof ProjectAlreadyExistsException) {
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
