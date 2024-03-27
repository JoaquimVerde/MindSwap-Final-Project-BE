package com.mindera.finalproject.be.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/teachers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeacherController {

    @Inject
    TeacherService teacherService;

    @GET
    public Response getTeachers() {
        return Response.ok(teacherService.findAll()).build();
    }

    @GET
    @Path("{teacherId}")
    public Response getTeacherById(Long id) {
        return Response.ok(teacherService.get(id)).build();
    }

    @POST
    public Response createTeacher(PersonCreateDto personCreateDto) {
        return Response.ok(teacherService.add(personCreateDto)).status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{teacherId}")
    public Response editTeacher(Long id, PersonCreateDto personCreateDto) {
        return Response.accepted(teacherService.edit(id, personCreateDto)).status(Response.Status.ACCEPTED).build();
    }

    @DELETE
    @Path("{teacherId}")
    public Response deleteTeacher(Long id) {
        return Response.ok(teacherService.delete(id)).build();
    }
}
