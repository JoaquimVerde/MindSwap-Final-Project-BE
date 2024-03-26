package com.mindera.finalproject.be.controller;

import com.mindera.finalproject.be.dto.student.StudentCreateDto;
import com.mindera.finalproject.be.service.StudentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path("/api/v1/students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentController {
    @Inject
    StudentService studentService;

    @GET
    public Response getStudents(){
        return Response.ok(studentService.findAll()).build();
    }

    @GET
    @Path("{studentId}")
    public Response getStudentById(Long id){
        return Response.ok(studentService.get(id)).build();
    }
    @POST
    public Response createStudent(StudentCreateDto studentCreateDto){
        return Response.ok(studentService.add(studentCreateDto)).status(Response.Status.CREATED).build();
    }
    @PUT
    @Path("{studentId}")
    public Response editStudent(Long id, StudentCreateDto studentCreateDto){
        return Response.accepted(studentService.edit(id, studentCreateDto)).status(Response.Status.ACCEPTED).build();
    }
    @DELETE
    @Path("{studentId}")
    public Response deleteStudent(Long id){
        return Response.ok(studentService.delete(id)).build();
    }






}
