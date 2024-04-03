package com.mindera.finalproject.be.controller;

import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.service.PersonService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/api/v1/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonController {

    @Inject
    private PersonService personService;

    @Operation(summary = "Find all persons")
    @APIResponse(responseCode = "200", description = "List of all persons")
    @GET
    public Response getAll() {
        return Response.ok(personService.getAll()).build();
    }

    @Operation(summary = "Find Person by id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Person found"),
            @APIResponse(responseCode = "404", description = "Person not found")
    })
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        return Response.ok(personService.getById(id)).build();
    }

    @Operation(summary = "Create a person")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Person created")
    })
    @POST
    public Response create(@Valid @RequestBody PersonCreateDto personCreateDto) {
        return Response.ok(personService.create(personCreateDto)).status(Response.Status.CREATED).build();
    }

    @Operation(summary = "Update a person")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Person updated"),
            @APIResponse(responseCode = "404", description = "Person not found")
    })
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, @Valid @RequestBody PersonCreateDto personCreateDto) {
        return Response.ok(personService.update(id, personCreateDto)).build();
    }

    @Operation(summary = "Delete a person")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Person deleted"),
            @APIResponse(responseCode = "404", description = "Person not found")
    })
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        personService.delete(id);
        return Response.ok().build();
    }
}
