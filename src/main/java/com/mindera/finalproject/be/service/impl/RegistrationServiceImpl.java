package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.converter.RegistrationConverter;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.dto.registration.RegistrationUpdateGradeDto;
import com.mindera.finalproject.be.dto.registration.RegistrationUpdateStatusDto;
import com.mindera.finalproject.be.email.Email;
import com.mindera.finalproject.be.entity.Registration;
import com.mindera.finalproject.be.exception.course.CourseNotFoundException;
import com.mindera.finalproject.be.exception.course.MaxNumberOfStudentsException;
import com.mindera.finalproject.be.exception.email.EmailGetTemplateException;
import com.mindera.finalproject.be.exception.pdf.PdfCreateException;
import com.mindera.finalproject.be.exception.registration.RegistrationAlreadyExistsException;
import com.mindera.finalproject.be.exception.registration.RegistrationNotFoundException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.RegistrationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mindera.finalproject.be.messages.Messages.REGISTRATION_ALREADY_EXISTS;
import static com.mindera.finalproject.be.messages.Messages.REGISTRATION_NOT_FOUND;

@ApplicationScoped
public class RegistrationServiceImpl implements RegistrationService {

    private final String TABLE_NAME = "Registration";
    private final String REGISTRATION = "REGISTRATION#";
    private final String GSIPK1 = "GSIPK1";
    private final String GSIPK2 = "GSIPK2";
    private final String ENROLLED = "ENROLLED";

    @Inject
    Email email;

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
    public List<RegistrationPublicDto> getAll(Integer page, Integer limit) {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(k -> k.partitionValue(REGISTRATION).sortValue(REGISTRATION));
        Expression expression = Expression.builder().expression("active = :active").putExpressionValue(":active", AttributeValue.fromBool(true)).build();
        QueryEnhancedRequest limitedQuery = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .filterExpression(expression)
                .limit(limit)
                .build();
        SdkIterable<Page<Registration>> registrations = registrationTable.query(limitedQuery);
        if (page >= registrations.stream().count()) {
            page = Math.toIntExact(registrations.stream().count() - 1);
        }
        List<Registration> registrationsList = new ArrayList<>(registrations.stream().toList().get(page).items());
        return registrationsList.stream().map(registration -> {
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
    public RegistrationPublicDto getById(String id) throws PersonNotFoundException, CourseNotFoundException, RegistrationNotFoundException {
        Registration registration = findById(id);
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
    public void delete(String id) throws RegistrationNotFoundException {
        Registration registration = findById(id);
        registration.setActive(false);
        registrationTable.updateItem(registration);
    }

    public Registration findById(String id) throws RegistrationNotFoundException {
        Registration registration = registrationTable.getItem(Key.builder().partitionValue(REGISTRATION).sortValue(id).build());
        if (registration == null) {
            throw new RegistrationNotFoundException(REGISTRATION_NOT_FOUND + id);
        }
        return registration;
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
    public List<RegistrationPublicDto> getRegistrationsByPerson(String personId, Integer page, Integer limit) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(personId));
        Expression expression = Expression.builder().expression("active = :active").putExpressionValue(":active", AttributeValue.fromBool(true)).build();
        QueryEnhancedRequest limitedQuery = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .filterExpression(expression)
                .limit(limit)
                .build();
        DynamoDbIndex<Registration> registrationIndex = registrationTable.index(GSIPK1);
        SdkIterable<Page<Registration>> registrations = registrationIndex.query(limitedQuery);
        List<Registration> registrationsList = new ArrayList<>(registrations.stream().toList().get(page).items());
        return registrationsList.stream().map(registration -> {
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
    public List<RegistrationPublicDto> getRegistrationsByCourse(String courseId, Integer page, Integer limit) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(courseId));
        Expression expression = Expression.builder().expression("active = :active").putExpressionValue(":active", AttributeValue.fromBool(true)).build();
        QueryEnhancedRequest limitedQuery = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .filterExpression(expression)
                .limit(limit)
                .build();
        DynamoDbIndex<Registration> registrationIndex = registrationTable.index(GSIPK2);
        SdkIterable<Page<Registration>> registrations = registrationIndex.query(limitedQuery);
        List<Registration> registrationsList = new ArrayList<>(registrations.stream().toList().get(page).items());
        return registrationsList.stream().map(registration -> {
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
    public RegistrationPublicDto updateStatus(String id, RegistrationUpdateStatusDto registrationUpdate) throws PersonNotFoundException, CourseNotFoundException, RegistrationNotFoundException, MaxNumberOfStudentsException, EmailGetTemplateException, PdfCreateException {
        Registration registration = findById(id);
        String oldStatus = registration.getStatus();

        registration.setStatus(registrationUpdate.status().replace(" ", "_").toUpperCase());

        if (!oldStatus.equals(ENROLLED) && registration.getStatus().equals(ENROLLED)) {
            courseService.updateEnrolledStudents(registration.getCourseId());
        }
        registrationTable.updateItem(registration);
        PersonPublicDto student = personService.getById(registration.getPersonId());
        CoursePublicDto course = courseService.getById(registration.getCourseId());
/*        if (registration.getStatus().equals("ACCEPTED") || registration.getStatus().equals("AUTOMATICALLY_ACCEPTED")) {
            email.sendCourseInvoice(personService.findById(registration.getPersonId()), courseService.findById(registration.getCourseId()));
        }*/
        email.sendCourseCandidatureStatusEmail(personService.findById(registration.getPersonId()), courseService.findById(registration.getCourseId()), registration);
        return RegistrationConverter.fromEntityToPublicDto(registration, student, course);
    }

    @Override
    public RegistrationPublicDto updateGrade(String id, RegistrationUpdateGradeDto registrationUpdate) throws PersonNotFoundException, CourseNotFoundException, RegistrationNotFoundException {
        Registration registration = findById(id);
        registration.setFinalGrade(registrationUpdate.grade());
        registrationTable.updateItem(registration);
        PersonPublicDto student = personService.getById(registration.getPersonId());
        CoursePublicDto course = courseService.getById(registration.getCourseId());
        return RegistrationConverter.fromEntityToPublicDto(registration, student, course);
    }
}