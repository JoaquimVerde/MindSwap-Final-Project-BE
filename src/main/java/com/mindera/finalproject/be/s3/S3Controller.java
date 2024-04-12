package com.mindera.finalproject.be.s3;

import com.mindera.finalproject.be.entity.Course;
import io.vertx.ext.web.FileUpload;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;

import java.io.File;

@Path("/api/v1/s3")
public class S3Controller {

    @Inject
    private S3Service s3Service;

    @POST
    @Path("/uploadProfileImage/{personId}")
    public Response uploadProfileImage(
            File file,
            @PathParam("personId") String personId) {
        return Response.ok(s3Service.uploadProfileImage(file, personId)).build();
    }

    @POST
    @Path("/uploadCV/{personId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadCV(@RestForm("file") File file, @PathParam("personId") String personId) {
        return Response.ok(s3Service.uploadCV(file, personId)).build();
    }

    @GET
    @Path("/getProfileImage/{personId}")
    public Response getProfileImage(@PathParam("personId") String personId) {
        return Response.ok(s3Service.getProfileImage(personId)).build();
    }

    @GET
    @Path("/invoice/{personId}/{courseId}")
    public Response downloadInvoice(@PathParam("personId") String personId, @PathParam("courseId") String courseId) {
        File file = s3Service.downloadInvoice(personId, courseId);
        return Response.ok(file).build();
    }

    @GET
    @Path("/certificate/{personId}/{courseId}")
    public Response downloadCertificate(@PathParam("personId") String personId, @PathParam("courseId") String courseId) {
        return Response.ok(s3Service.downloadCertificate(personId, courseId)).build();
    }
}
