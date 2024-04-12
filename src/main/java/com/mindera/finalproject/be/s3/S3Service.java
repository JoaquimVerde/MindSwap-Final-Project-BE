package com.mindera.finalproject.be.s3;

import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.exception.pdf.PdfCreateException;
import com.mindera.finalproject.be.pdf.Pdf;
import io.vertx.ext.web.FileUpload;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.http.apache.ApacheHttpClient;


import java.io.File;
import java.net.URI;
import java.nio.file.Files;

@ApplicationScoped
public class S3Service {


    S3Client s3 = S3Client.builder()
            .region(Region.EU_CENTRAL_1)   
            .httpClientBuilder(ApacheHttpClient.builder())
            .build();

    @Inject
    Pdf pdfGenerator;

    @ConfigProperty(name = "bucket.name")
    private String bucketName;


    public Response uploadProfileImage(File file, String personId) {
        if (file == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        String objectKey = constructProfilePictureObjectKey(personId);
        PutObjectResponse putResponse = s3.putObject(buildImgPutRequest(objectKey), RequestBody.fromFile(file));
        if (putResponse != null) {
            return Response.ok().status(Response.Status.CREATED).build();
        } else {
            return Response.serverError().build();
        }
    }

    public Response uploadCV(File file, String personId) {
        if (file == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        String objectKey = constructCVObjectKey(personId);
        PutObjectResponse putResponse = s3.putObject(buildPdfPutRequest(objectKey), RequestBody.fromFile(file));
        if (putResponse != null) {
            return Response.ok().status(Response.Status.CREATED).build();
        } else {
            return Response.serverError().build();
        }
    }

    public byte[] uploadCertificate(Person person, Course course) throws PdfCreateException {
        byte[] pdfBytes = pdfGenerator.generateCertificatePdf(person, course);
        String ObjectKey = constructCertificateObjectKey(person.getSK(), course.getSK());
        PutObjectResponse putResponse = s3.putObject(buildPdfPutRequest(ObjectKey), RequestBody.fromBytes(pdfBytes));
        if (putResponse != null) {
            System.out.println("Certificate uploaded successfully");
        }
        return pdfBytes;
    }

    public byte[] uploadInvoice(Person person, Course course) throws PdfCreateException {
        byte[] pdfBytes = pdfGenerator.generateInvoicePdf(person, course);
        String ObjectKey = constructInvoiceObjectKey(person.getSK(), course.getSK());
        PutObjectResponse putResponse = s3.putObject(buildPdfPutRequest(ObjectKey), RequestBody.fromBytes(pdfBytes));
        if (putResponse != null) {
            System.out.println("Invoice uploaded successfully");
        }
        return pdfBytes;
    }

    public File downloadInvoice(String personId, String courseId) {
        String certificateObjectKey = constructInvoiceObjectKey(personId, courseId);
        return downloadPdfFile(certificateObjectKey);
    }

    public File downloadCV(String personId) {
        String certificateObjectKey = constructCVObjectKey(personId);
        return downloadPdfFile(certificateObjectKey);
    }

    public File downloadCertificate(String personId, String courseId) {
        String certificateObjectKey = "certificates-" + personId + "_" + courseId;
        return downloadPdfFile(certificateObjectKey);
    }

    public File getProfileImage(String personId) {
        String certificateObjectKey = "profilePic-" + personId;
        return downloadImgFile(certificateObjectKey);
    }


    private File downloadImgFile(String objectKey) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(request);
            byte[] fileBytes = objectBytes.asByteArray();
            File tempFile = File.createTempFile(objectKey, ".jpg");
            Files.write(tempFile.toPath(), fileBytes);
            return tempFile;
        } catch (Exception e) {
            return null;
        }
    }

    private File downloadPdfFile(String objectKey) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();
            ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(request);
            byte[] fileBytes = objectBytes.asByteArray();
            File tempFile = File.createTempFile(objectKey, ".pdf");
            Files.write(tempFile.toPath(), fileBytes);
            return tempFile;
        } catch (Exception e) {
            return null;
        }
    }

    private String constructProfilePictureObjectKey(String personId) {
        return "profilePic-" + personId;
    }

    private String constructCVObjectKey(String personId) {
        return "cv-" + personId + ".pdf";
    }

    private String constructCertificateObjectKey(String personId, String courseId) {
        return "certificates-" + personId + "_" + courseId + ".pdf";
    }

    private String constructInvoiceObjectKey(String personId, String courseId) {
        return "invoice-" + personId + "_" + courseId + ".pdf";
    }

    protected PutObjectRequest buildImgPutRequest(String objectKey) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType("application/jpg")
                .build();
    }

    protected PutObjectRequest buildPdfPutRequest(String objectKey) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType("application/pdf")
                .build();
    }

    protected GetObjectRequest buildGetRequest(String objectKey) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
    }

}
