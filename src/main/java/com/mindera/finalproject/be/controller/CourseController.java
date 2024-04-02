package com.mindera.finalproject.be.controller;

import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.service.CourseService;
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
    private CourseService courseService;

    @Operation(summary = "Find all courses")
    @APIResponse(responseCode = "200", description = "List of all courses")
    @GET
    public Response getAll() {
        return Response.ok(courseService.getAll()).build();
    }

    @Operation(summary = "Find course by id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Course found"),
            @APIResponse(responseCode = "404", description = "Course not found")
    })
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        return Response.ok(courseService.getById(id)).build();
    }

    @Operation(summary = "Create a course")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Course created")
    })
    @POST
    public Response create(@Valid @RequestBody CourseCreateDto courseCreateDto) {
        return Response.ok(courseService.create(courseCreateDto)).status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/location/{location}")
    public Response getByLocation(@PathParam("location") String location) {
        return Response.ok(courseService.getByLocation(location)).build();

    }

    @Operation(summary = "Update a course")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Course updated"),
            @APIResponse(responseCode = "404", description = "Course not found")
    })
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, @Valid @RequestBody CourseCreateDto courseCreateDto) {
        return Response.ok(courseService.update(id, courseCreateDto)).build();
    }

    @Operation(summary = "Delete a course")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Course deleted"),
            @APIResponse(responseCode = "404", description = "Course not found")
    })
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        courseService.delete(id);
        return Response.ok().build();
    }
}
