package com.mindera.finalproject.be.s3;

import com.mindera.finalproject.be.exception.pdf.PdfCreateException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.pdf.Pdf;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;


import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class S3SyncClientResource {
    @Inject
    S3Client s3;
    @Inject
    Pdf pdfGenerator;
    @ConfigProperty(name = "s3.bucket")
    private String bucketName;

    public File uploadInvoice(Person person, Course course) throws PdfCreateException {
        byte[] pdfBytes = pdfGenerator.generateInvoicePdf(person, course);
        String invoiceObjectKey = constructInvoiceObjectKey(person.getSK(), course.getSK());
        System.out.println(s3.listBuckets());
        PutObjectResponse putResponse = s3.putObject(buildPutRequest(invoiceObjectKey), RequestBody.fromBytes(pdfBytes));
        if (putResponse != null) {
            System.out.println("Invoice uploaded successfully");
        }
        return downloadFile(invoiceObjectKey);
    }

    public File uploadCertificate(Person person, Course course) throws PdfCreateException {
        byte[] pdfBytes = pdfGenerator.generateCertificatePdf(person, course);
        String certificateObjectKey = constructCertificateObjectKey(person.getSK(), course.getSK());
        PutObjectResponse putResponse = s3.putObject(buildPutRequest(certificateObjectKey), RequestBody.fromBytes(pdfBytes));
        if (putResponse != null) {
            System.out.println("Certificate uploaded successfully");
        }
        return downloadFile(certificateObjectKey);
    }

    public File downloadInvoice(String personId, String courseId) {
        String invoiceObjectKey = constructInvoiceObjectKey(personId, courseId);
        return downloadFile(invoiceObjectKey);
    }

    public File downloadCertificate(String personId, String courseId) {
        String certificateObjectKey = constructCertificateObjectKey(personId, courseId);
        return downloadFile(certificateObjectKey);
    }

    public List<String> listInvoices() {
        return listFiles("invoices/");
    }

    public List<String> listCertificates() {
        return listFiles("certificates/");
    }

    private String constructInvoiceObjectKey(String personId, String courseId) {
        return "invoices/" + personId + "_" + courseId + ".pdf";
    }

    private String constructCertificateObjectKey(String personId, String courseId) {
        return "certificates/" + personId + "_" + courseId + ".pdf";
    }

    private List<String> listFiles(String prefix) {
        ListObjectsRequest listRequest = ListObjectsRequest.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();

        ListObjectsResponse listResponse = s3.listObjects(listRequest);
        List<String> objectKeys = listResponse.contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());

        return objectKeys;
    }

    private File downloadFile(String objectKey) {
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


    protected PutObjectRequest buildPutRequest(String objectKey) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
    }

    protected GetObjectRequest buildGetRequest(String objectKey) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
    }

}