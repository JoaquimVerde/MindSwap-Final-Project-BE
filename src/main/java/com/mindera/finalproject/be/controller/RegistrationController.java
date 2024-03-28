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
    public Response getRegistrations() {
        return Response.ok(registrationService.getAll()).build();
    }


    @Path("/create")
    @POST
    public Response createRegistration(RegistrationCreateDto registrationCreateDto) {

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
    public Response getRegistration(@PathParam("id") String id) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        System.out.println("ID: " + id);
        return Response.ok(registrationService.getByCompositeKey(id)).build();
    }

    @Path("/{id}/edit")
    @PUT
    public Response editRegistration(@PathParam("id") String id, RegistrationCreateDto registrationCreateDto) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        return Response.ok(registrationService.update(id, registrationCreateDto)).build();
    }

    @Path("/{id}/delete")
    @DELETE
    public Response deleteRegistration(@PathParam("id") String id) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        return Response.ok(registrationService.delete(id)).build();
    }

}