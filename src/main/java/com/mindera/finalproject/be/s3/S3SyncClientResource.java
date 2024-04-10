package com.mindera.finalproject.be.s3;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class S3SyncClientResource extends CommonResource {

    @Inject
    S3Client s3;

    public Response uploadFile(FormData formData) throws Exception {

        if (formData.filename == null || formData.filename.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (formData.contentType == null || formData.contentType.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        PutObjectResponse putResponse = s3.putObject(buildPutRequest(formData),
                RequestBody.fromFile(formData.data));
        if (putResponse != null) {
            return Response.ok().status(Response.Status.CREATED).build();
        } else {
            return Response.serverError().build();
        }
    }
    
    public Response downloadFile(String objectKey) {
        ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(buildGetRequest(objectKey));
        Response.ResponseBuilder response = Response.ok(objectBytes.asByteArray());
        response.header("Content-Disposition", "attachment;filename=" + objectKey);
        response.header("Content-Type", objectBytes.response().contentType());
        return response.build();
    }

    public List<FileObject> listFiles() {
        ListObjectsRequest listRequest = ListObjectsRequest.builder().bucket(bucketName).build();
        return s3.listObjects(listRequest).contents().stream()
                .map(FileObject::from)
                .sorted(Comparator.comparing(FileObject::getObjectKey))
                .collect(Collectors.toList());
    }

}
