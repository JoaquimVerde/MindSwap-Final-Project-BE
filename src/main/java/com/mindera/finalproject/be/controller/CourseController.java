package com.mindera.finalproject.be.controller;

import com.mindera.finalproject.be.dto.course.courseCreateDto;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/api/v1/courses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CourseController {

    @Inject
    CourseService courseService;

    @Operation(summary = "Find all courses")
    @APIResponse(responseCode = "200", description = "List of all courses")
    @GET
    public Response findAll() {
        return Response.ok(courseService.findAll()).build();
    }

    @Operation(summary = "Find course by id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Course found"),
            @APIResponse(responseCode = "404", description = "Course not found")
    })
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) throws CourseNotFoundException{
        return Response.ok(courseService.findById(id)).build();
    }

    @Operation(summary = "Create a course")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Course created")
    })
    @POST
    public Response create(@Valid @RequestBody courseCreateDto courseCreateDto) {
        return Response.ok(courseService.create(courseCreateDto)).build();
    }

    @Operation(summary = "Update a course")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Course updated"),
            @APIResponse(responseCode = "404", description = "Course not found")
    })
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid @RequestBody courseCreateDto courseCreateDto) throws CourseNotFoundException {
        return Response.ok(courseService.edit(id, courseCreateDto)).build();
    }

    @Operation(summary = "Delete a course")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Course deleted"),
            @APIResponse(responseCode = "404", description = "Course not found")
    })
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) throws CourseNotFoundException {
        return Response.ok(courseService.delete(id)).build();
    }
}
