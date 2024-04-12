package com.mindera.finalproject.be.S3;

import com.mindera.finalproject.be.s3.S3Service;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class S3ControllerTests {

    @Inject
    private S3Client s3;
    @ConfigProperty(name = "bucket.name")
    private String bucketName;


    @Test
    public void testUploadProfileImage() {

        File file = new File("src/main/resources/cat.jpg");
        String personId = "test1234";
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(personId + ".jpg")
                .build();
        s3.putObject(putObjectRequest, file.toPath());
    }

    @Test
    public void testUploadCV() {
        File file = new File("src/main/resources/Fábio_CV2024.pdf");
        String personId = "test1234";
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(personId + ".pdf")
                .build();
        s3.putObject(putObjectRequest, file.toPath());
    }
}
