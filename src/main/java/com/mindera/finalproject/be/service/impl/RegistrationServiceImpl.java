package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.converter.RegistrationConverter;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.entity.Registration;
import com.mindera.finalproject.be.exception.course.CourseNotFoundException;
import com.mindera.finalproject.be.exception.registration.RegistrationAlreadyExistsException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.RegistrationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mindera.finalproject.be.messages.Messages.REGISTRATION_ALREADY_EXISTS;

@ApplicationScoped
public class RegistrationServiceImpl implements RegistrationService {

    private final String TABLE_NAME = "Registration";
    private final String REGISTRATION = "REGISTRATION#";
    private final String GSIPK1 = "GSIPK1";

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
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return RegistrationConverter.fromEntityToPublicDto(registration, student, course);
        }).toList();
    }

    @Override
    public RegistrationPublicDto getById(String id) throws PersonNotFoundException, CourseNotFoundException {
        Registration registration = registrationTable.getItem(Key.builder().partitionValue(REGISTRATION).sortValue(id).build());
        String personId = registration.getPersonId();
        String courseId = registration.getCourseId();
        PersonPublicDto student = personService.getById(personId);
        CoursePublicDto course = courseService.getById(courseId);
        return RegistrationConverter.fromEntityToPublicDto(registration, student, course);
    }

    @Override
    public RegistrationPublicDto create(RegistrationCreateDto registrationCreateDto) throws PersonNotFoundException, CourseNotFoundException, RegistrationAlreadyExistsException {
        Registration registration = RegistrationConverter.fromCreateDtoToEntity(registrationCreateDto);
        if (checkIfRegistrationIsDuplicate(registration)) {
            throw new RegistrationAlreadyExistsException(REGISTRATION_ALREADY_EXISTS);
        }
        registration.setPK(REGISTRATION);
        registration.setSK(REGISTRATION + UUID.randomUUID());
        registration.setStatus(registrationCreateDto.status().replace(" ", "_").toUpperCase());
        PersonPublicDto student = personService.getById(registration.getPersonId());
        CoursePublicDto course = courseService.getById(registration.getCourseId());
        registrationTable.putItem(registration);
        return RegistrationConverter.fromEntityToPublicDto(registration, student, course);
    }

    @Override
    public RegistrationPublicDto update(String id, RegistrationCreateDto registrationCreateDto) throws PersonNotFoundException, CourseNotFoundException {
        Registration oldRegistration = registrationTable.getItem(Key.builder().partitionValue(id).sortValue(id).build());

        oldRegistration.setPK(oldRegistration.getPK());
        oldRegistration.setSK(oldRegistration.getSK());
        oldRegistration.setStatus(registrationCreateDto.status().replace(" ", "_").toUpperCase());
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

    private boolean checkIfRegistrationIsDuplicate(Registration registration) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(registration.getPersonId()).sortValue(registration.getCourseId()));
        DynamoDbIndex<Registration> index = registrationTable.index(GSIPK1);
        SdkIterable<Page<Registration>> registrations = index.query(queryConditional);
        List<Registration> registrationsList = new ArrayList<>();
        registrations.forEach(page -> registrationsList.addAll(page.items()));
        return registrationsList.size() > 0;
    }

    @Override
    public List<RegistrationPublicDto> getRegistrationsByPerson(String personId) {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(k -> k.partitionValue(REGISTRATION).sortValue(REGISTRATION));
        SdkIterable<Page<Registration>> registrations = registrationTable.query(queryConditional);
        List<Registration> registrationsList = new ArrayList<>();
        registrations.forEach(page -> registrationsList.addAll(page.items()));
        return registrationsList.stream().filter(registration -> registration.getPersonId().equals(personId) && registration.getActive()).map(registration -> {
            PersonPublicDto student = null;
            CoursePublicDto course = null;
            try {
                student = personService.getById(registration.getPersonId());
                course = courseService.getById(registration.getCourseId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return RegistrationConverter.fromEntityToPublicDto(registration, student, course);
        }).toList();
    }

    @Override
    public List<RegistrationPublicDto> getRegistrationsByCourse(String courseId) {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(k -> k.partitionValue(REGISTRATION).sortValue(REGISTRATION));
        SdkIterable<Page<Registration>> registrations = registrationTable.query(queryConditional);
        List<Registration> registrationsList = new ArrayList<>();
        registrations.forEach(page -> registrationsList.addAll(page.items()));
        return registrationsList.stream().filter(registration -> registration.getCourseId().equals(courseId) && registration.getActive()).map(registration -> {
            PersonPublicDto student = null;
            CoursePublicDto course = null;
            try {
                student = personService.getById(registration.getPersonId());
                course = courseService.getById(registration.getCourseId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return RegistrationConverter.fromEntityToPublicDto(registration, student, course);
        }).toList();
    }

}