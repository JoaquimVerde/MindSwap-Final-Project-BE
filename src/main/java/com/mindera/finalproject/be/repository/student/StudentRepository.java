package com.mindera.finalproject.be.repository.student;

import com.mindera.finalproject.be.entity.Student;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;


import java.time.LocalDate;

import static com.mindera.finalproject.be.repository.student.DbConfig.enhancedClient;

@ApplicationScoped
public class StudentRepository{

    static final TableSchema<Student> studentTableSchema =
            TableSchema.builder(Student.class)
                    .newItemSupplier(Student::new)
                    .addAttribute(Long.class, a -> a.name("id")
                            .getter(Student::getId)
                            .setter(Student::setId)
                            .tags(StaticAttributeTags.primaryPartitionKey()))
                    .addAttribute(String.class, a -> a.name("email")
                            .getter(Student::getEmail)
                            .setter(Student::setEmail)
                            .tags(StaticAttributeTags.primarySortKey()))
                    .addAttribute(String.class, a -> a.name("first name")
                            .getter(Student::getFirstName)
                            .setter(Student::setFirstName))
                    .addAttribute(String.class, a -> a.name("last name")
                            .getter(Student::getLastName)
                            .setter(Student::setLastName))
                    .addAttribute(String.class, a -> a.name("role")
                            .getter(Student::getRole)
                            .setter(Student::setRole))
                    .addAttribute(String.class, a -> a.name("username")
                            .getter(Student::getUsername)
                            .setter(Student::setUsername))
                    .addAttribute(LocalDate.class, a -> a.name("date of birth")
                            .getter(Student::getDateOfBirth)
                            .setter(Student::setDateOfBirth))
                    .addAttribute(Integer.class, a -> a.name("age")
                            .getter(Student::getAge))
                    .addAttribute(String.class, a -> a.name("address")
                            .getter(Student::getAddress)
                            .setter(Student::setAddress))
                    .addAttribute(String.class, a -> a.name("curriculum")
                            .getter(Student::getCurriculum)
                            .setter(Student::setCurriculum))
                    .build();
    DynamoDbTable<Student> table = enhancedClient.table("TestTable2", studentTableSchema);

    public TestRep() {
//        table.createTable();
    }

    public void save(TestEntity testEntity) {
        table.putItem(testEntity);
    }

    public List<TestEntity> getAll() {
        return table.scan().items().stream().toList();
    }
}

