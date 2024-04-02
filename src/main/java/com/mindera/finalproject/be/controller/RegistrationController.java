package com.mindera.finalproject.be.controller;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.service.RegistrationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/api/v1/registration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegistrationController {

    @Inject
    RegistrationService registrationService;


    @GET
    public Response getAll() {
        return Response.ok(registrationService.getAll()).build();
    }

    @POST
    public Response create(RegistrationCreateDto registrationCreateDto) {

        String registrationId = UUID.randomUUID().toString();

        RegistrationCreateDto newRegistrationCreateDto = new RegistrationCreateDto(
                registrationId,
                registrationCreateDto.personId(),
                registrationCreateDto.courseId(),
                registrationCreateDto.status(),
                registrationCreateDto.finalGrade(),
                registrationCreateDto.active()
        );


        return Response.ok(registrationService.create(newRegistrationCreateDto)).build();
    }

    @Path("/{id}")
    @GET
    public Response getById(@PathParam("id") String id) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        return Response.ok(registrationService.getById(id)).build();
    }

    @Path("/{id}")
    @PUT
    public Response update(@PathParam("id") String id, RegistrationCreateDto registrationCreateDto) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        return Response.ok(registrationService.update(id, registrationCreateDto)).build();
    }

    @Path("/{id}")
    @DELETE
    public Response deleteRegistration(@PathParam("id") String id) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        return Response.ok().build();
    }

}