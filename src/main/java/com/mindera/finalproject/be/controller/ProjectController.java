package com.mindera.finalproject.be.controller;


import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectController {


    // inject service

    @Path("/")
    @GET
    public Response findAll(){
        return Response.ok().build();
    }

    @Path("/{id}")
    @GET
        public Response findById(@QueryParam("id") Long id){
            return Response.ok().build();
    }


    @POST
    public Response createProject(ProjectCreateDto ProjectCreateDto){
        return Response.ok().build();
    }


    @PUT
    @Path("/{id}")
    public ProjectCreateDto editProject(@QueryParam("id") Long id, ProjectCreateDto ProjectCreateDto){
        return ProjectCreateDto;
    }


    @DELETE
    @Path("/{id}")
    public Response deleteProject(@QueryParam("id") Long id){
        return Response.ok().build();
    }
}


