package com.mindera.finalproject.be.service.impl;


import com.mindera.finalproject.be.converter.CourseConverter;
import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.exception.course.CourseAlreadyExistsException;
import com.mindera.finalproject.be.exception.course.CourseNotFoundException;
import com.mindera.finalproject.be.exception.course.MaxNumberOfStudentsException;
import com.mindera.finalproject.be.exception.student.PersonNotATeacherException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.CourseService;
import com.mindera.finalproject.be.service.PersonService;
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

import static com.mindera.finalproject.be.messages.Messages.*;

@ApplicationScoped
public class CourseServiceImpl implements CourseService {

    private final String TABLE_NAME = "Course";
    private final String COURSE = "COURSE#";
    private final String GSIPK1 = "GSIPK1";
    private final String GSIPK2 = "GSIPK2";
    private final String TEACHER = "TEACHER";
    private final Integer MAX_STUDENTS = 20;

    @Inject
    private PersonService personService;
    private DynamoDbTable<Course> courseTable;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        courseTable = dynamoEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Course.class));
    }

    @Override
    public List<CoursePublicDto> getAll(Integer page, Integer limit) {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(k -> k.partitionValue(COURSE).sortValue(COURSE));
        Expression expression = Expression.builder().expression("active = :active").putExpressionValue(":active", AttributeValue.fromBool(true)).build();
        QueryEnhancedRequest limitedQuery = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .filterExpression(expression)
                .limit(limit)
                .build();
        SdkIterable<Page<Course>> courses = courseTable.query(limitedQuery);
        List<Course> coursesList = new ArrayList<>(courses.stream().toList().get(page).items());
        return coursesList.stream().map(this::mapCourseList).toList();
    }

    @Override
    public CoursePublicDto getById(String id) throws PersonNotFoundException, CourseNotFoundException {
        Course course = findById(id);
        if (course.getTeacherId() == null || !personService.findById(course.getTeacherId()).isActive()) {
            return CourseConverter.fromEntityToPublicDto(course, null);
        }
        return CourseConverter.fromEntityToPublicDto(course, personService.getById(course.getTeacherId()));
    }

    @Override
    public CoursePublicDto create(CourseCreateDto courseCreateDto) throws PersonNotFoundException, CourseAlreadyExistsException, PersonNotATeacherException {
        Course course = CourseConverter.fromCreateDtoToEntity(courseCreateDto);
        if (checkIfCourseIsDuplicate(course)) {
            throw new CourseAlreadyExistsException(COURSE_ALREADY_EXISTS);
        }
        if (courseCreateDto.teacherId() != null) {
            if (courseCreateDto.teacherId().isEmpty()) {
                course.setTeacherId(null);
            } else {
                checkIfPersonIsValid(courseCreateDto.teacherId());
            }
        }
        course.setPK(COURSE);
        course.setSK(COURSE + UUID.randomUUID());
        course.setMaxStudents(MAX_STUDENTS);
        course.setLocation(courseCreateDto.location().toUpperCase());
        courseTable.putItem(course);
        return course.getTeacherId() == null ? CourseConverter.fromEntityToPublicDto(course, null) : CourseConverter.fromEntityToPublicDto(course, personService.getById(course.getTeacherId()));
    }

    @Override
    public List<CoursePublicDto> createSeveralCourses(List<CourseCreateDto> courseCreateDtos) throws PersonNotFoundException, CourseAlreadyExistsException, PersonNotATeacherException {
        List<CoursePublicDto> courses = new ArrayList<>();
        for (CourseCreateDto courseCreateDto : courseCreateDtos) {
            courses.add(create(courseCreateDto));
        }
        return courses;
    }

    @Override
    public List<CoursePublicDto> getByLocation(String location, Integer page, Integer limit) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(location.toUpperCase()));
        Expression expression = Expression.builder().expression("active = :active").putExpressionValue(":active", AttributeValue.fromBool(true)).build();
        QueryEnhancedRequest limitedQuery = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .filterExpression(expression)
                .limit(limit)
                .build();
        DynamoDbIndex<Course> courseIndex = courseTable.index(GSIPK1);
        SdkIterable<Page<Course>> courses = courseIndex.query(limitedQuery);
        List<Course> coursesList = new ArrayList<>(courses.stream().toList().get(page).items());
        return coursesList.stream().map(this::mapCourseList).toList();
    }

    @Override
    public CoursePublicDto update(String id, CourseCreateDto courseCreateDto) throws PersonNotFoundException, CourseNotFoundException, PersonNotATeacherException {
        Course course = findById(id);
        course.setTeacherId(courseCreateDto.teacherId());
        if (courseCreateDto.teacherId() != null) {
            if (courseCreateDto.teacherId().isEmpty()) {
                course.setTeacherId(null);
            } else {
                checkIfPersonIsValid(courseCreateDto.teacherId());
            }
        }
        course.setName(courseCreateDto.name());
        course.setEdition(courseCreateDto.edition());
        course.setSyllabus(courseCreateDto.syllabus());
        course.setProgram(courseCreateDto.program());
        course.setSchedule(courseCreateDto.schedule());
        course.setPrice(courseCreateDto.price());
        course.setDuration(courseCreateDto.duration());
        course.setLocation(courseCreateDto.location());

        courseTable.putItem(course);
        if (course.getTeacherId() == null) {
            return CourseConverter.fromEntityToPublicDto(course, null);
        }
        return CourseConverter.fromEntityToPublicDto(course, personService.getById(course.getTeacherId()));
    }

    @Override
    public void delete(String id) throws CourseNotFoundException {
        Course course = findById(id);
        course.setActive(false);
        courseTable.updateItem(course);
    }

    @Override
    public Course findById(String id) throws CourseNotFoundException {
        Course course = courseTable.getItem(Key.builder().partitionValue(COURSE).sortValue(id).build());
        if (course == null) {
            throw new CourseNotFoundException(COURSE_NOT_FOUND + id);
        }
        return course;
    }

    @Override
    public List<CoursePublicDto> findCoursesByTeacher(String id) throws PersonNotFoundException {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(id));
        DynamoDbIndex<Course> index = courseTable.index(GSIPK2);
        SdkIterable<Page<Course>> courses = index.query(queryConditional);
        List<Course> coursesList = new ArrayList<>();
        courses.forEach(page -> coursesList.addAll(page.items()));
        return coursesList.stream().map(this::mapCourseList).toList();
    }

    private CoursePublicDto mapCourseList(Course course) {
        try {
            if (course.getTeacherId() == null || !personService.findById(course.getTeacherId()).isActive()) {
                return CourseConverter.fromEntityToPublicDto(course, null);
            }
            return CourseConverter.fromEntityToPublicDto(course, personService.getById(course.getTeacherId()));
        } catch (PersonNotFoundException e) {
            return CourseConverter.fromEntityToPublicDto(course, null);
        }
    }

    private boolean checkIfCourseIsDuplicate(Course course) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(course.getName()).sortValue(course.getEdition()));
        DynamoDbIndex<Course> index = courseTable.index(GSIPK2);
        SdkIterable<Page<Course>> courses = index.query(queryConditional);
        List<Course> coursesList = new ArrayList<>();
        courses.forEach(page -> coursesList.addAll(page.items()));
        return !coursesList.isEmpty();
    }

    private void checkIfPersonIsValid(String personId) throws PersonNotFoundException, PersonNotATeacherException {
        if (personService.findById(personId) == null) {
            throw new PersonNotFoundException(PERSON_NOT_FOUND + personId);
        }
        if (!personService.findById(personId).getRole().equals(TEACHER)){
            throw new PersonNotATeacherException(PERSON_NOT_A_TEACHER);
        }
    }

    void updateEnrolledStudents(String id) throws CourseNotFoundException, MaxNumberOfStudentsException {
        Course course = findById(id);
        if (course.getEnrolledStudents() >= course.getMaxStudents()) {
            throw new MaxNumberOfStudentsException(MAX_NUMBER_OF_STUDENTS_REACHED);
        }
        course.setEnrolledStudents(course.getEnrolledStudents() + 1);
        courseTable.updateItem(course);
    }
}
