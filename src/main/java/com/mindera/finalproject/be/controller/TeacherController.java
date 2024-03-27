package com.mindera.finalproject.be.controller;

import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.service.TeacherService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/api/v1/teachers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeacherController {

    @Inject
    TeacherService teacherService;

    @Operation(summary = "Get all teachers")
    @APIResponse(responseCode = "200", description = "List of all teachers")
    @GET
    public Response getTeachers() {
        return Response.ok(teacherService.findAll()).build();
    }

    @Operation(summary = "Get teacher by id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Teacher found"),
            @APIResponse(responseCode = "404", description = "Teacher not found")
    })
    @GET
    @Path("/{teacherId}")
    public Response getTeacherById(@PathParam("id") Long id) {
        return Response.ok(teacherService.get(id)).build();
    }

    @Operation(summary = "Create a teacher")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Teacher created")
    })
    @POST
    public Response createTeacher(PersonCreateDto personCreateDto) {
        return Response.ok(teacherService.add(personCreateDto)).status(Response.Status.CREATED).build();
    }

    @Operation(summary = "Update a teacher")
    @APIResponses(value = {
            @APIResponse(responseCode = "202", description = "Teacher updated"),
            @APIResponse(responseCode = "404", description = "Teacher not found")
    })
    @PUT
    @Path("/{teacherId}")
    public Response editTeacher(@PathParam("id") Long id, PersonCreateDto personCreateDto) {
        return Response.accepted(teacherService.edit(id, personCreateDto)).status(Response.Status.ACCEPTED).build();
    }

    @Operation(summary = "Delete a teacher")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Teacher deleted"),
            @APIResponse(responseCode = "404", description = "Teacher not found")
    })
    @DELETE
    @Path("/{teacherId}")
    public Response deleteTeacher(@PathParam("id") Long id) {
        return Response.ok(teacherService.delete(id)).build();
    }
}
