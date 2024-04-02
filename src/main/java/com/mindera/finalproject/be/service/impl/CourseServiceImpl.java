package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.TableCreation.TableCreation;
import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.service.CourseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CourseServiceImpl implements CourseService {

    private DynamoDbTable<Course> courseTable;

    @Inject
    TableCreation tableCreation;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        courseTable = dynamoEnhancedClient.table("Course", TableSchema.fromBean(Course.class));
    }

    @Override
    public List<Course> getAll() {
        return courseTable.scan().items().stream().collect(Collectors.toList());
    }

    @Override
    public Course getById(String id) {
        return courseTable.getItem(Key.builder().partitionValue(id).build());
    }

    @Override
    public Course create(Course course) {
        course.setSK("000003");
        course.setPK("000000003");
        courseTable.putItem(course);
        return course;
    }

    @Override
    public List<Course> getByLocation(String location) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(location));
        QueryEnhancedRequest queryEnhancedRequest = QueryEnhancedRequest.builder().queryConditional(queryConditional).build();
        return courseTable.query(queryEnhancedRequest).items().stream().toList();
    }

    @Override
    public Course update(String id, CourseCreateDto coursePublicDto) {
        Course course = new Course();
        courseTable.putItem(course);
        return course;
    }

    @Override
    public void delete(String id) {
        courseTable.deleteItem(Key.builder().partitionValue(id).build());
    }

}
