package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.TableCreation.TableCreation;
import com.mindera.finalproject.be.converter.RegistrationConverter;
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
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class RegistrationServiceImpl implements RegistrationService {

    private final String TABLE_NAME = "Registration";
    private final String GSIPK = "GSIPK";
    private DynamoDbTable<Registration> registrationTable;

    @Inject
    TableCreation tableCreation;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        registrationTable = dynamoEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Registration.class));
    }

    @Override
    public List<RegistrationPublicDto> getAll() {
        return RegistrationConverter.fromEntityListToPublicDtoList(registrationTable.scan().items().stream().toList());
    }

    @Override
    public RegistrationPublicDto getById(String id) {
        return RegistrationConverter.fromEntityToPublicDto(registrationTable.getItem(Key.builder().partitionValue(id).sortValue(id).build()));
    }

    @Override
    public RegistrationPublicDto create(RegistrationCreateDto registrationCreateDto) {
        Registration registration = RegistrationConverter.fromCreateDtoToEntity(registrationCreateDto);
        String id = "REGISTRATION#" + UUID.randomUUID();
        registration.setPK(id);
        registration.setSK(id);
        registrationTable.putItem(registration);
        return RegistrationConverter.fromEntityToPublicDto(registration);
    }

    @Override
    public RegistrationPublicDto update(String id, RegistrationCreateDto registrationCreateDto) {
        Registration oldRegistration = registrationTable.getItem(Key.builder().partitionValue(id).sortValue(id).build());

        // status
        if(!registrationCreateDto.status().equals(oldRegistration.getStatus())){
            oldRegistration.setStatus(registrationCreateDto.status());
        }

        //finalGrade
        if(!registrationCreateDto.finalGrade().equals(oldRegistration.getFinalGrade())){
            oldRegistration.setFinalGrade(registrationCreateDto.finalGrade());
        }

        //active
        if(!registrationCreateDto.active().equals(oldRegistration.getActive())){
            oldRegistration.setActive(registrationCreateDto.active());
        }

        //aboutYou
        if(!registrationCreateDto.aboutYou().equals(oldRegistration.getAboutYou())){
            oldRegistration.setAboutYou(registrationCreateDto.aboutYou());
        }

        //prevKnowledge
        if(!registrationCreateDto.prevKnowledge().equals(oldRegistration.getPrevKnowledge())){
            oldRegistration.setPrevKnowledge(registrationCreateDto.prevKnowledge());
        }

        //prevExperience
        if(!registrationCreateDto.prevExperience().equals(oldRegistration.getPrevExperience())){
            oldRegistration.setPrevExperience(registrationCreateDto.prevExperience());
        }

        registrationTable.putItem(oldRegistration);

        return RegistrationConverter.fromEntityToPublicDto(oldRegistration);
    }

    @Override
    public void delete(String id) {
        Registration registration = registrationTable.getItem(Key.builder().partitionValue(id).build());
        registration.setActive(false);
    }

}