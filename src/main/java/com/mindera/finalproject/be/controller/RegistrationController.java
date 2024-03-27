package com.mindera.finalproject.be.controller;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.service.impl.RegistrationServiceImpl;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path("/api/v1/registration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegistrationController {

    @Inject
    private RegistrationServiceImpl registrationService;

    @GET
    public Response getRegistrations() {
        return Response.ok(registrationService.findAll()).build();
    }

    @Path("/{id}")
    @GET
    public Response getRegistration(@PathParam("id") String id) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        return Response.ok(registrationService.findByCompositeKey(id)).build();
    }

    @POST
    public Response createRegistration(@Valid @RequestBody RegistrationCreateDto registrationCreateDto) {
        return Response.ok(registrationService.createRegistration(registrationCreateDto)).status(Response.Status.CREATED).build();
    }

    @Path("/{id}")
    @PUT
    public Response editRegistration(@PathParam("id") String id, @Valid @RequestBody RegistrationCreateDto registrationCreateDto) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        return Response.ok(registrationService.updateRegistration(id, registrationCreateDto)).build();
    }

    @Path("/{id}")
    @DELETE
    public Response deleteRegistration(@PathParam("id") String id) { //Este id é personId#courseId, deve ser concatenado antes de chegar aqui
        return Response.ok(registrationService.deleteRegistration(id)).build();
    }
}
