package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.entity.Registration;
import com.mindera.finalproject.be.service.RegistrationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RegistrationServiceImpl implements RegistrationService {

    private DynamoDbTable<Registration> registrationTable;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        registrationTable = dynamoEnhancedClient.table("Registration", TableSchema.fromBean(Registration.class));
    }

    @Override
    public List<RegistrationPublicDto> getAll() {
        //TODO convert List<Registration> to List<RegistrationPublicDto>
        registrationTable.scan().items().stream().collect(Collectors.toList());
        return null;
    }

    @Override
    public RegistrationPublicDto getById(String id) {
        registrationTable.getItem(Key.builder().partitionValue(id).build());
        //TODO convert Registration to RegistrationPublicDto
        return null;
    }

    @Override
    public Registration create(RegistrationCreateDto registrationCreateDto) {
        //TODO convert registrationCreateDto to Registration
        Registration registration = new Registration();
        registrationTable.putItem(registration);
        return registration;
    }

    @Override
    public RegistrationPublicDto update(String id, RegistrationCreateDto registrationCreateDto) {
        //TODO convert registrationCreateDto to Registration
        Registration registration = new Registration();
        registrationTable.putItem(registration);
        return null;
    }

    @Override
    public void delete(String id) {
        registrationTable.deleteItem(Key.builder().partitionValue(id).build());
    }

}