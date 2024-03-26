package com.mindera.finalproject.be.controller;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.service.impl.RegistrationServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/registration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegistrationController {

    @Inject
    RegistrationServiceImpl registrationService;



    @GET
    public Response getRegistrations() {
        return Response.ok(registrationService.getRegistrations()).build();
    }


    @Path("/api/v1/registration/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response createRegistration(RegistrationCreateDto registrationCreateDto) {
        return Response.ok(registrationService.createRegistration(registrationCreateDto)).build();
    }

    @Path("/api/v1/registration/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    public Response getRegistration(@PathParam("id") String id) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        return Response.ok(registrationService.getRegistrationByCompositeKey(id)).build();
    }

    @Path("/api/v1/registration/{id}/edit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Response editRegistration(@PathParam("id") String id, RegistrationCreateDto registrationCreateDto) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        return Response.ok(registrationService.updateRegistration(id, registrationCreateDto)).build();
    }

    @Path("/api/v1/registration/{id}/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    public Response deleteRegistration(@PathParam("id") String id) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        return Response.ok(registrationService.deleteRegistration(id)).build();
    }

}
