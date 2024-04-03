package com.mindera.finalproject.be;

import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Project;
import com.mindera.finalproject.be.entity.Registration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@ApplicationScoped
public class TestTables {

    public DynamoDbTable<Course> courseTable;

    public DynamoDbTable<Person> personTable;

    public DynamoDbTable<Project> projectTable;

    public DynamoDbTable<Registration> registrationTable;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        courseTable = dynamoEnhancedClient.table("Course", TableSchema.fromBean(Course.class));
        personTable = dynamoEnhancedClient.table("Person", TableSchema.fromBean(Person.class));
        projectTable = dynamoEnhancedClient.table("Project", TableSchema.fromBean(Project.class));
        registrationTable = dynamoEnhancedClient.table("Registration", TableSchema.fromBean(Registration.class));
    }

}
