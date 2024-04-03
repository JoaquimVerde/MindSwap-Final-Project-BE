package com.mindera.finalproject.be.service.impl;


import com.mindera.finalproject.be.TableCreation.TableCreation;

import com.mindera.finalproject.be.converter.CourseConverter;

import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.entity.Course;
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
    private final String TABLE2_NAME = "Person";
    private final String GSIPK = "GSIPK";
    private DynamoDbTable<Course> courseTable;

    @Inject
    TableCreation tableCreation;

    @Inject
    PersonService personService;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        courseTable = dynamoEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Course.class));
    }

    @Override
    public List<CoursePublicDto> getAll() {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(k -> k.partitionValue("COURSE#").sortValue("COURSE#"));
        SdkIterable<Page<Course>> courses = courseTable.query(queryConditional);
        List<Course> coursesList = new ArrayList<>();
        courses.forEach(page -> coursesList.addAll(page.items()));
        return coursesList.stream().filter(Course::getActive).map(course -> {
            if (course.getTeacherId() == null) {
                return CourseConverter.fromEntityToPublicDto(course, null);
            }
            return CourseConverter.fromEntityToPublicDto(course, personService.getById(course.getTeacherId()));
        }).toList();
    }

    @Override
    public CoursePublicDto getById(String id) {
        Course course = courseTable.getItem(Key.builder().partitionValue(id).sortValue(id).build());
        if (course.getTeacherId() == null) {
            return CourseConverter.fromEntityToPublicDto(course, null);
        }
        return CourseConverter.fromEntityToPublicDto(course, personService.getById(course.getTeacherId()));
    }

    @Override
    public CoursePublicDto create(CourseCreateDto courseCreateDto) {
        Course course = CourseConverter.fromCreateDtoToEntity(courseCreateDto);
        String id = "COURSE#" + UUID.randomUUID();
        course.setPK(id);
        course.setSK(id);
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
            return CourseConverter.fromEntityToPublicDto(course, personService.getById(course.getTeacherId()));
        }).toList();
    }

    @Override
    public CoursePublicDto update(String id, CourseCreateDto coursePublicDto) {
        Course course = new Course();
        courseTable.putItem(course);
        return CourseConverter.fromEntityToPublicDto(course, personService.getById(course.getTeacherId()));
    }

    @Override
    public void delete(String id) {
        Course course = courseTable.getItem(Key.builder().partitionValue(id).sortValue(id).build());
        course.setActive(false);
        courseTable.updateItem(course);
    }

}
