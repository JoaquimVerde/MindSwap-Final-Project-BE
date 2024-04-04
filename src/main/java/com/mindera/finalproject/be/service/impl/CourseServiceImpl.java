package com.mindera.finalproject.be.service.impl;


import com.mindera.finalproject.be.converter.CourseConverter;
import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.CourseService;
import com.mindera.finalproject.be.service.PersonService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CourseServiceImpl implements CourseService {

    private final String TABLE_NAME = "Course";
    private final String COURSE = "COURSE#";
    private final String GSIPK = "GSIPK";
    //    @Inject
    //    TableCreation tableCreation;
    @Inject
    PersonService personService;
    private DynamoDbTable<Course> courseTable;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        courseTable = dynamoEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Course.class));
    }

    @Override
    public List<CoursePublicDto> getAll() {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(k -> k.partitionValue(COURSE).sortValue(COURSE));
        SdkIterable<Page<Course>> courses = courseTable.query(queryConditional);
        List<Course> coursesList = new ArrayList<>();
        courses.forEach(page -> coursesList.addAll(page.items()));
        return coursesList.stream().filter(Course::getActive).map(course -> {
            if (course.getTeacherId() == null) {
                return CourseConverter.fromEntityToPublicDto(course, null);
            }
            try {
                return CourseConverter.fromEntityToPublicDto(course, personService.getById(course.getTeacherId()));
            } catch (PersonNotFoundException e) {
                return CourseConverter.fromEntityToPublicDto(course, null);
            }
        }).toList();
    }

    @Override
    public CoursePublicDto getById(String id) throws PersonNotFoundException {
        Course course = findById(id);
        if (course.getTeacherId() == null) {
            return CourseConverter.fromEntityToPublicDto(course, null);
        }
        return CourseConverter.fromEntityToPublicDto(course, personService.getById(course.getTeacherId()));
    }

    @Override
    public CoursePublicDto create(CourseCreateDto courseCreateDto) throws PersonNotFoundException {
        Person teacher = personService.findById(courseCreateDto.teacherId());
        Course course = CourseConverter.fromCreateDtoToEntity(courseCreateDto);
        if (teacher == null) {
            course.setTeacherId(null);
        }
        course.setPK(COURSE);
        course.setSK(COURSE + UUID.randomUUID());
        courseTable.putItem(course);
        return course.getTeacherId() == null ? CourseConverter.fromEntityToPublicDto(course, null) : CourseConverter.fromEntityToPublicDto(course, personService.getById(course.getTeacherId()));
    }

    @Override
    public List<CoursePublicDto> getByLocation(String location) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(location));
        DynamoDbIndex<Course> courseIndex = courseTable.index(GSIPK);
        SdkIterable<Page<Course>> courses = courseIndex.query(queryConditional);
        List<Course> coursesList = new ArrayList<>();
        courses.forEach(page -> coursesList.addAll(page.items()));
        return coursesList.stream().filter(Course::getActive).map(course -> {
            if (course.getTeacherId() == null) {
                return CourseConverter.fromEntityToPublicDto(course, null);
            }
            try {
                return CourseConverter.fromEntityToPublicDto(course, personService.getById(course.getTeacherId()));
            } catch (PersonNotFoundException e) {
                return CourseConverter.fromEntityToPublicDto(course, null);
            }
        }).toList();
    }

    @Override
    public CoursePublicDto update(String id, CourseCreateDto coursePublicDto) throws PersonNotFoundException {
        Course course = new Course();
        courseTable.putItem(course);
        return CourseConverter.fromEntityToPublicDto(course, personService.getById(course.getTeacherId()));
    }

    @Override
    public void delete(String id) {
        Course course = courseTable.getItem(Key.builder().partitionValue(COURSE).sortValue(id).build());
        course.setActive(false);
        courseTable.updateItem(course);
    }

    @Override
    public Course findById(String id) {
        return courseTable.getItem(Key.builder().partitionValue(COURSE).sortValue(id).build());
    }

}
