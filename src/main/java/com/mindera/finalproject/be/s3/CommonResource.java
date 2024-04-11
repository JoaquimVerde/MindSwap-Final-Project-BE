/*
package com.mindera.finalproject.be.s3;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

abstract public class CommonResource {
    @ConfigProperty(name = "s3.bucket")
    String bucketName;

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
*/
