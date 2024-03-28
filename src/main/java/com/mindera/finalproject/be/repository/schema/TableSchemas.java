package com.mindera.finalproject.be.repository.schema;

import com.mindera.finalproject.be.entity.Teacher;
import jakarta.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;

import java.time.LocalDate;

@Singleton
public class TableSchemas {
//    Insert TableSchemas here

    public static final TableSchema<Teacher> teacherTableSchema =
            TableSchema.builder(Teacher.class)
                    .newItemSupplier(Teacher::new)
                    .addAttribute(Long.class, a -> a.name("id")
                            .getter(Teacher::getId)
                            .setter(Teacher::setId)
                            .tags(StaticAttributeTags.primaryPartitionKey()))
                    .addAttribute(String.class, a -> a.name("email")
                            .getter(Teacher::getEmail)
                            .setter(Teacher::setEmail)
                            .tags(StaticAttributeTags.primarySortKey()))
                    .addAttribute(String.class, a -> a.name("firstName")
                            .getter(Teacher::getFirstName)
                            .setter(Teacher::setFirstName))
                    .addAttribute(String.class, a -> a.name("lastName")
                            .getter(Teacher::getLastName)
                            .setter(Teacher::setLastName))
                    .addAttribute(String.class, a -> a.name("role")
                            .getter(Teacher::getRole)
                            .setter(Teacher::setRole))
                    .addAttribute(String.class, a -> a.name("username")
                            .getter(Teacher::getUsername)
                            .setter(Teacher::setUsername))
                    .addAttribute(LocalDate.class, a -> a.name("dateOfBirth")
                            .getter(Teacher::getDateOfBirth)
                            .setter(Teacher::setDateOfBirth))
                    .addAttribute(Integer.class, a -> a.name("age")
                            .getter(Teacher::getAge))
                    .addAttribute(String.class, a -> a.name("address")
                            .getter(Teacher::getAddress)
                            .setter(Teacher::setAddress))
                    .build();
}
