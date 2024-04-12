package com.mindera.finalproject.be.s3;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.io.File;

@Path("/api/v1/s3")
public class S3Controller {

    @Inject
    private S3Service s3Service;

    @Operation(summary = "Upload profile image")
    @APIResponse(responseCode = "200", description = "Profile image uploaded")
    @POST
    @Path("/uploadProfileImage/{personId}")
    public Response uploadProfileImage(
            File file,
            @PathParam("personId") String personId) {
        s3Service.uploadProfileImage(file, personId);
        return Response.ok().build();
    }

    @Operation(summary = "Upload CV")
    @APIResponse(responseCode = "200", description = "CV uploaded")
    @POST
    @Path("/uploadCV/{personId}")
    public Response uploadCV(
            File file,
            @PathParam("personId") String personId) {
        return Response.ok(s3Service.uploadCV(file, personId)).build();
    }

    @Operation(summary = "Get profile image")
    @APIResponse(responseCode = "200", description = "Profile image downloaded")
    @GET
    @Path("/getProfileImage/{personId}")
    public Response getProfileImage(
            @PathParam("personId") String personId) {
        return Response.ok(s3Service.getProfileImage(personId)).build();
    }

    @Operation(summary = "Get CV")
    @APIResponse(responseCode = "200", description = "CV downloaded")
    @GET
    @Path("/getCV/{personId}")
    public Response getCV(
            @PathParam("personId") String personId) {
        return Response.ok(s3Service.downloadCV(personId)).build();
    }

    @Operation(summary = "Download invoice")
    @APIResponse(responseCode = "200", description = "Invoice downloaded")
    @GET
    @Path("/invoice/{personId}/{courseId}")
    public Response downloadInvoice(
            @PathParam("personId") String personId,
            @PathParam("courseId") String courseId) {
        return Response.ok(s3Service.downloadInvoice(personId, courseId)).build();
    }

    @Operation(summary = "Download certificate")
    @APIResponse(responseCode = "200", description = "Certificate downloaded")
    @GET
    @Path("/certificate/{personId}/{courseId}")
    public Response downloadCertificate(
            @PathParam("personId") String personId,
            @PathParam("courseId") String courseId) {
        return Response.ok(s3Service.downloadCertificate(personId, courseId)).build();
    }
}
