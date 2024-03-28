package com.mindera.finalproject.be.repository.student;

import jakarta.inject.Singleton;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Singleton
public class DbConfig {

    static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(
                    // Configure an instance of the standard client.
                    DynamoDbClient.builder()
                            .region(Region.EU_CENTRAL_1)
                            .credentialsProvider(ProfileCredentialsProvider.create())
                            .build())
            .build();
}

