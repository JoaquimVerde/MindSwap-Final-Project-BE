package com.mindera.finalproject.be.service.impl;


import com.mindera.finalproject.be.TableCreation.TableCreation;

import com.mindera.finalproject.be.converter.CourseConverter;

import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.service.CourseService;
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
    private final String GSIPK = "GSIPK";
    private DynamoDbTable<Course> courseTable;

    @Inject
    TableCreation tableCreation;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        courseTable = dynamoEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Course.class));
    }

    @Override
    public List<CoursePublicDto> getAll() {
        return CourseConverter.fromEntityListToPublicDtoList(courseTable.scan().items().stream().toList());
    }

    @Override
    public CoursePublicDto getById(String id) {
        return CourseConverter.fromEntityToPublicDto(courseTable.getItem(Key.builder().partitionValue(id).build()));
    }

    @Override
    public CoursePublicDto create(CourseCreateDto courseCreateDto) {
        Course course = CourseConverter.fromCreateDtoToEntity(courseCreateDto);
        String id = "COURSE#" + UUID.randomUUID();
        course.setPK(id);
        course.setSK(id);
        courseTable.putItem(course);
        return CourseConverter.fromEntityToPublicDto(course);
    }

    @Override
    public List<CoursePublicDto> getByLocation(String location) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(location));
        DynamoDbIndex<Course> courseIndex = courseTable.index(GSIPK);
        SdkIterable<Page<Course>> courses = courseIndex.query(queryConditional);
        List<Course> coursesList = new ArrayList<>();
        courses.forEach(page -> coursesList.addAll(page.items()));
        return CourseConverter.fromEntityListToPublicDtoList(coursesList);
    }

    @Override
    public CoursePublicDto update(String id, CourseCreateDto coursePublicDto) {
        Course course = new Course();
        courseTable.putItem(course);
        return CourseConverter.fromEntityToPublicDto(course);
    }

    @Override
    public void delete(String id) {
        courseTable.deleteItem(Key.builder().partitionValue(id).build());
    }

}
