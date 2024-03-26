package com.mindera.finalproject.be.controller;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.service.impl.RegistrationServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/registration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegistrationController {

    @Inject
    RegistrationServiceImpl registrationService;



    @GET
    public Response getRegistrations() {
        return Response.ok(registrationService.getRegistrations()).build();
    }


    @Path("/registration/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response createRegistration(RegistrationCreateDto registrationCreateDto) {
        return Response.ok(registrationService.createRegistration(registrationCreateDto)).build();
    }

    @Path("/registration/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    public Response getRegistration(@PathParam("id") Long id) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        return Response.ok(registrationService.getRegistration(id)).build();
    }

    @Path("/registration/{id}/edit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Response editRegistration(@PathParam("id") Long id, RegistrationCreateDto registrationCreateDto) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        return Response.ok(registrationService.editRegistration(id, registrationCreateDto)).build();
    }

    @Path("/registration/{id}/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    public Response deleteRegistration(@PathParam("id") Long id) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        return Response.ok(registrationService.deleteRegistration(id)).build();
    }

}
