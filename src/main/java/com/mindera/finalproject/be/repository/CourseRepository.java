package com.mindera.finalproject.be.repository;

import com.mindera.finalproject.be.entity.Course;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.List;

import static com.mindera.finalproject.be.config.DynamoDbConfig.client;
import static com.mindera.finalproject.be.repository.schema.TableSchemas.courseTableSchema;


@ApplicationScoped
public class CourseRepository {

    DynamoDbTable<Course> table = client.table("courseTable", courseTableSchema);

    public CourseRepository() {
        table.createTable();
    }

    public List<Course> getAll() {
        return table.scan().items().stream().toList();
    }

    public void save(Course course) {
        table.putItem(course);
    }

    public Course getById(String registrationId) {
        return table.getItem(Key.builder().partitionValue(registrationId).build());
    }

    public void deleteById(String registrationId) {
        table.deleteItem(Key.builder().partitionValue(registrationId).build());
    }

    public Course update(Course registration) {
        table.updateItem(registration);
        return registration;
    }

}
