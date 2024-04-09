package com.mindera.finalproject.be.controller;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationUpdateGradeDto;
import com.mindera.finalproject.be.dto.registration.RegistrationUpdateStatusDto;
import com.mindera.finalproject.be.exception.course.CourseNotFoundException;
import com.mindera.finalproject.be.exception.course.MaxNumberOfStudentsException;
import com.mindera.finalproject.be.exception.registration.RegistrationAlreadyExistsException;
import com.mindera.finalproject.be.exception.registration.RegistrationNotFoundException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.RegistrationService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/api/v1/registration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegistrationController {

    @Inject
    RegistrationService registrationService;

    @Operation(summary = "Find all registrations")
    @APIResponse(responseCode = "200", description = "List of all registrations")
    @GET
    public Response getAll(@QueryParam("page") @DefaultValue("0") Integer page, @QueryParam("limit") @DefaultValue("100") Integer limit) {
        return Response.ok(registrationService.getAll(page, limit)).build();
    }

    @Operation(summary = "Find registration by id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Registration found"),
            @APIResponse(responseCode = "404", description = "Registration not found")
    })
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) throws PersonNotFoundException, CourseNotFoundException, RegistrationNotFoundException {
        return Response.ok(registrationService.getById(id)).build();
    }

    @Operation(summary = "Create a registration")
    @APIResponse(responseCode = "201", description = "Registration created")
    @POST
    public Response create(@Valid @RequestBody RegistrationCreateDto registrationCreateDto)
            throws PersonNotFoundException, CourseNotFoundException, RegistrationAlreadyExistsException {
        return Response.ok(registrationService.create(registrationCreateDto)).status(Response.Status.CREATED).build();
    }

    @Operation(summary = "Update a registration")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Registration updated"),
            @APIResponse(responseCode = "404", description = "Registration not found")
    })
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, @Valid @RequestBody RegistrationCreateDto registrationCreateDto)
            throws PersonNotFoundException, CourseNotFoundException, MaxNumberOfStudentsException {
        return Response.ok(registrationService.update(id, registrationCreateDto)).build();
    }

    @Operation(summary = "Delete a registration")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Registration deleted"),
            @APIResponse(responseCode = "404", description = "Registration not found")
    })
    @DELETE
    @Path("/delete/{id}")
    public Response deleteRegistration(@PathParam("id") String id) {
        registrationService.delete(id);
        return Response.ok().build();
    }
    @GET
    @Path("/student/{personId}")
    public Response getByPersonId(@PathParam("personId") String personId) {
        return Response.ok(registrationService.getRegistrationsByPerson(personId)).build();
    }

    @GET
    @Path("course/{courseId}")
    public Response getByCourseId(@PathParam("courseId") String courseId) {
        return Response.ok(registrationService.getRegistrationsByCourse(courseId)).build();
    }

    @PUT
    @Path("/status/{id}")
    public Response updateStatus(@PathParam("id") String id, @RequestBody @Valid RegistrationUpdateStatusDto registrationUpdate)
            throws PersonNotFoundException, CourseNotFoundException, RegistrationNotFoundException, MaxNumberOfStudentsException {
        return Response.ok(registrationService.updateStatus(id, registrationUpdate)).build();
    }

    @PUT
    @Path("/grade/{id}")
    public Response updateGrade(@PathParam("id") String id, @RequestBody @Valid RegistrationUpdateGradeDto registrationUpdate)
            throws PersonNotFoundException, CourseNotFoundException, RegistrationNotFoundException {
        return Response.ok(registrationService.updateGrade(id, registrationUpdate)).build();
    }
}