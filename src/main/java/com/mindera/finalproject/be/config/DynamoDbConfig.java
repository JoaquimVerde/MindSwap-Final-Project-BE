package com.mindera.finalproject.be.config;

import jakarta.inject.Singleton;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Singleton
public class DynamoDbConfig {

    public static DynamoDbEnhancedClient client = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(
                    DynamoDbClient.builder()
                            .region(Region.EU_CENTRAL_1)
                            .credentialsProvider(ProfileCredentialsProvider.create())
                            .build()
            )
            .build();
}
