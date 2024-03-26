package com.mindera.finalproject.be.controller;

import com.mindera.finalproject.be.dto.course.courseCreateDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path("/api/v1/courses")
public class CourseController {

    @GET
    public Response findAll() {
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return Response.ok().build();
    }

    @POST
    public Response create(@Valid @RequestBody courseCreateDto courseCreateDto) {
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid @RequestBody courseCreateDto courseCreateDto) {
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return Response.ok().build();
    }
}
