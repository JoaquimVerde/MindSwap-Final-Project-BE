package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.TableCreation.TableCreation;
import com.mindera.finalproject.be.converter.RegistrationConverter;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
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
    private DynamoDbTable<Person> personTable;
    private DynamoDbTable<Course> courseTable;

    @Inject
    TableCreation tableCreation;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        registrationTable = dynamoEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Registration.class));
        personTable = dynamoEnhancedClient.table("Person", TableSchema.fromBean(Person.class));
        courseTable = dynamoEnhancedClient.table("Course", TableSchema.fromBean(Course.class));
    }

    @Override
    public List<RegistrationPublicDto> getAll() {
        List<Registration> registrations = registrationTable.scan().items().stream().filter(Registration::getActive).toList();

        return registrations.stream().map(registration -> {
            String personId = registration.getPersonId();
            String courseId = registration.getCourseId();
            Person student = personTable.getItem(Key.builder().partitionValue(personId).sortValue(personId).build()) != null ? personTable.getItem(Key.builder().partitionValue(personId).build()) : null;
            Course course = courseTable.getItem(Key.builder().partitionValue(courseId).sortValue(courseId).build()) != null ? courseTable.getItem(Key.builder().partitionValue(courseId).build()) : null;
            return RegistrationConverter.fromEntityToPublicDto(registration, student, course);
        }).toList();
    }

    @Override
    public RegistrationPublicDto getById(String id) {
        Registration registration = registrationTable.getItem(Key.builder().partitionValue(id).sortValue(id).build());
        String personId = registration.getPersonId();
        String courseId = registration.getCourseId();
        //Person student = personTable.getItem(Key.builder().partitionValue(personId).sortValue(personId).build()) != null ? personTable.getItem(Key.builder().partitionValue(personId).build()) : null;
        //Course course = courseTable.getItem(Key.builder().partitionValue(courseId).sortValue(personId).build()) != null ? courseTable.getItem(Key.builder().partitionValue(courseId).build()) : null;
        Person student = new Person();
        Course course = new Course();
        return RegistrationConverter.fromEntityToPublicDto(registration, student, course);
    }

    @Override
    public RegistrationPublicDto create(RegistrationCreateDto registrationCreateDto) {
        Registration registration = RegistrationConverter.fromCreateDtoToEntity(registrationCreateDto);
        String id = "REGISTRATION#" + UUID.randomUUID();
        registration.setPK(id);
        registration.setSK(id);
        Person student = personTable.getItem(Key.builder().partitionValue(registration.getPersonId()).build());
        Course course = courseTable.getItem(Key.builder().partitionValue(registration.getCourseId()).build());
        registrationTable.putItem(registration);
        return RegistrationConverter.fromEntityToPublicDto(registration, student, course);
    }

    @Override
    public RegistrationPublicDto update(String id, RegistrationCreateDto registrationCreateDto) {
        Registration oldRegistration = registrationTable.getItem(Key.builder().partitionValue(id).sortValue(id).build());

        oldRegistration.setPK(oldRegistration.getPK());
        oldRegistration.setSK(oldRegistration.getSK());
        oldRegistration.setStatus(registrationCreateDto.status());
        oldRegistration.setFinalGrade(registrationCreateDto.finalGrade());
        oldRegistration.setAboutYou(registrationCreateDto.aboutYou());
        oldRegistration.setPrevKnowledge(registrationCreateDto.prevKnowledge());
        oldRegistration.setPrevExperience(registrationCreateDto.prevExperience());

        Person student = personTable.getItem(Key.builder().partitionValue(oldRegistration.getPersonId()).build());
        Course course = courseTable.getItem(Key.builder().partitionValue(oldRegistration.getCourseId()).build());

        registrationTable.putItem(oldRegistration);

        return RegistrationConverter.fromEntityToPublicDto(oldRegistration, student, course);
    }

    @Override
    public void delete(String id) {
        Registration registration = registrationTable.getItem(Key.builder().partitionValue(id).sortValue(id).build());
        registration.setActive(false);
        registrationTable.updateItem(registration);
    }

}