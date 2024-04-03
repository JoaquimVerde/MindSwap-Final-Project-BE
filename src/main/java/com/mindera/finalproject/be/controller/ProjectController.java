package com.mindera.finalproject.be.controller;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.exception.course.CourseNotFoundException;
import com.mindera.finalproject.be.exception.project.ProjectNotFoundException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.ProjectService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;


@Path("/api/v1/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectController {

    @Inject
    ProjectService projectService;


    @Operation(summary = "Find all projects")
    @APIResponse(responseCode = "200", description = "List of all projects")
    @GET
    public Response getAll() {
        return Response.ok(projectService.getAll()).build();
    }


    @Operation(summary = "Find project by id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Project found"),
            @APIResponse(responseCode = "404", description = "Project not found")
    })
    @Path("/{id}")
    @GET
    public Response getById(@PathParam("id") String id) throws ProjectNotFoundException, PersonNotFoundException {
        return Response.ok(projectService.getById(id)).build();
    }


    @Operation(summary = "Create new project")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Project created")
    })
    @POST
    public Response create(@Valid @RequestBody ProjectCreateDto projectCreateDto) throws ProjectNotFoundException, PersonNotFoundException, CourseNotFoundException {
        return Response.ok(projectService.create(projectCreateDto)).status(Response.Status.CREATED).build();
    }


    @Operation(summary = "Update a project")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Project edited"),
            @APIResponse(responseCode = "404", description = "Project not found")
    })
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, @Valid @RequestBody ProjectCreateDto projectCreateDto) throws PersonNotFoundException, ProjectNotFoundException {
        return Response.ok(projectService.update(id, projectCreateDto)).build();
    }


    @Operation(summary = "Delete a project")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Project deleted"),
            @APIResponse(responseCode = "404", description = "Project not found")
    })
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) throws ProjectNotFoundException {
        projectService.delete(id);
        return Response.ok().build();
    }
}


