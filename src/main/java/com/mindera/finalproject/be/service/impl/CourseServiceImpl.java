package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.service.CourseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CourseServiceImpl implements CourseService {

    private DynamoDbTable<Course> courseTable;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        courseTable = dynamoEnhancedClient.table("Course", TableSchema.fromBean(Course.class));
    }

    @Override
    public List<Course> getAll() {
        return courseTable.scan().items().stream().collect(Collectors.toList());
    }

    @Override
    public CoursePublicDto getById(String id) {
        return courseTable.getItem(Key.builder().partitionValue(id).build());
    }

    @Override
    public CoursePublicDto create(CourseCreateDto coursePublicDto) {
        Course course = new Course();
        courseTable.putItem(course);
        return course;
    }

    @Override
    public CoursePublicDto update(String id, CourseCreateDto coursePublicDto) {
        Course course = new Course();
        courseTable.putItem(course);
        return course;
    }

    @Override
    public void delete(String id) {
        courseTable.deleteItem(Key.builder().partitionValue(id).build());
    }

}
