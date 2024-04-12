/*
package com.mindera.finalproject.be.S3;


import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.net.URI;


@QuarkusTest
public class S3ControllerTests {

    private S3Client s3;
    private String bucketName = "quarkusbucket";

    @BeforeEach
    public void setup() {
        s3 = S3Client.builder()
                .endpointOverride(URI.create("https://s3.amazonaws.com"))
                .region(Region.EU_WEST_2)
                .httpClientBuilder(ApacheHttpClient.builder())
                .build();
    }

    @Test
    public void testUploadProfileImage() {

        File file = new File("src/test/resources/test.txt");
        String personId = "test1234";
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(personId + ".txt")
                .build();
        s3.putObject(putObjectRequest, file.toPath());
    }


}*/
