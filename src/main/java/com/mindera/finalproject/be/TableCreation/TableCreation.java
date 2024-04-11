/*

package com.mindera.finalproject.be.TableCreation;


import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Project;
import com.mindera.finalproject.be.entity.Registration;
import io.quarkus.runtime.Startup;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Startup
@Singleton
public class TableCreation {
    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        try {
            DynamoDbTable<Course> courseTable = dynamoEnhancedClient.table("Course", TableSchema.fromBean(Course.class));
            courseTable.createTable();
            DynamoDbTable<Person> personTable = dynamoEnhancedClient.table("Person", TableSchema.fromBean(Person.class));
            personTable.createTable();
            DynamoDbTable<Project> projectTable = dynamoEnhancedClient.table("Project", TableSchema.fromBean(Project.class));
            projectTable.createTable();
            DynamoDbTable<Registration> registrationTable = dynamoEnhancedClient.table("Registration", TableSchema.fromBean(Registration.class));
            registrationTable.createTable();
        } catch (Exception e) {
            System.out.println("Table already exists");
        }
    }
}


*/
