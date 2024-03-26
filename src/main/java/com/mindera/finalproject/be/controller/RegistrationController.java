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

    @POST
    public Response createRegistration(RegistrationCreateDto registrationCreateDto) {
        return Response.ok(registrationService.createRegistration(registrationCreateDto)).build();
    }

    @GET
    public Response getRegistrations() {
        return Response.ok(registrationService.getRegistrations()).build();
    }

}
