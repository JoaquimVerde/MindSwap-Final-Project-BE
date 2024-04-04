package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.TableCreation.TableCreation;
import com.mindera.finalproject.be.converter.RegistrationConverter;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.entity.Registration;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.RegistrationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RegistrationServiceImpl implements RegistrationService {

    private final String TABLE_NAME = "Registration";
    private final String REGISTRATION = "REGISTRATION#";
    @Inject
    PersonServiceImpl personService;
    @Inject
    CourseServiceImpl courseService;
    private DynamoDbTable<Registration> registrationTable;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        registrationTable = dynamoEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Registration.class));
    }

    @Override
    public List<RegistrationPublicDto> getAll() {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(k -> k.partitionValue(REGISTRATION).sortValue(REGISTRATION));
        SdkIterable<Page<Registration>> registrations = registrationTable.query(queryConditional);
        List<Registration> registrationsList = new ArrayList<>();
        registrations.forEach(page -> registrationsList.addAll(page.items()));
        return registrationsList.stream().filter(Registration::getActive).map(registration -> {
            PersonPublicDto student = null;
            CoursePublicDto course = null;
            try {
                student = personService.getById(registration.getPersonId());
                course = courseService.getById(registration.getCourseId());
            } catch (PersonNotFoundException e) {
                throw new RuntimeException(e);
            }
            return RegistrationConverter.fromEntityToPublicDto(registration, student, course);
        }).toList();
    }

    @Override
    public RegistrationPublicDto getById(String id) throws PersonNotFoundException {
        Registration registration = registrationTable.getItem(Key.builder().partitionValue(REGISTRATION).sortValue(id).build());
        String personId = registration.getPersonId();
        String courseId = registration.getCourseId();
        PersonPublicDto student = personService.getById(personId);
        CoursePublicDto course = courseService.getById(courseId);
        return RegistrationConverter.fromEntityToPublicDto(registration, student, course);
    }

    @Override
    public RegistrationPublicDto create(RegistrationCreateDto registrationCreateDto) throws PersonNotFoundException {
        Registration registration = RegistrationConverter.fromCreateDtoToEntity(registrationCreateDto);
        registration.setPK(REGISTRATION);
        registration.setSK(REGISTRATION + UUID.randomUUID());
        PersonPublicDto student = personService.getById(registration.getPersonId());
        CoursePublicDto course = courseService.getById(registration.getCourseId());
        registrationTable.putItem(registration);
        return RegistrationConverter.fromEntityToPublicDto(registration, student, course);
    }

    @Override
    public RegistrationPublicDto update(String id, RegistrationCreateDto registrationCreateDto) throws PersonNotFoundException {
        Registration oldRegistration = registrationTable.getItem(Key.builder().partitionValue(id).sortValue(id).build());

        oldRegistration.setPK(oldRegistration.getPK());
        oldRegistration.setSK(oldRegistration.getSK());
        oldRegistration.setStatus(registrationCreateDto.status());
        oldRegistration.setFinalGrade(registrationCreateDto.finalGrade());
        oldRegistration.setAboutYou(registrationCreateDto.aboutYou());
        oldRegistration.setPrevKnowledge(registrationCreateDto.prevKnowledge());
        oldRegistration.setPrevExperience(registrationCreateDto.prevExperience());

        PersonPublicDto student = personService.getById(oldRegistration.getPersonId());
        CoursePublicDto course = courseService.getById(oldRegistration.getCourseId());

        registrationTable.putItem(oldRegistration);

        return RegistrationConverter.fromEntityToPublicDto(oldRegistration, student, course);
    }

    @Override
    public void delete(String id) {
        Registration registration = registrationTable.getItem(Key.builder().partitionValue(REGISTRATION).sortValue(id).build());
        registration.setActive(false);
        registrationTable.updateItem(registration);
    }

}