package com.mindera.finalproject.be.repository.schema;


import com.mindera.finalproject.be.entity.Registration;
import jakarta.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

@Singleton
public class TableSchemas {


//    Insert TableSchemas here

    public static final TableSchema<Registration> registrationTableSchema =
            TableSchema.builder(Registration.class)
            .newItemSupplier(Registration::new)
                .addAtrribute(String.class, a -> a.name("registrationId")
                        .getter(Registration::getRegistrationId)
                        .setter(Registration::setRegistrationId)
                        .tags(StaticAttributeTags.primaryPartitionKey()))
                .addAttribute(String.class, a -> a.name("compositeKey")
                        .getter(Registration::getCompositeKey)
                        .setter(Registration::setCompositeKey)
                        .tags(StaticAttributeTags.primarySortKey()))
                .addAttribute(Long.class, a -> a.name("personId")
                        .getter(Registration::getPersonId)
                        .setter(Registration::setPersonId))
                .addAttribute(Long.class, a -> a.name("courseId")
                        .getter(Registration::getCourseId)
                        .setter(Registration::setCourseId))
                .addAttribute(String.class, a -> a.name("status")
                        .getter(Registration::getStatus)
                        .setter(Registration::setStatus))
                .addAttribute(String.class, a -> a.name("finalGrade")
                        .getter(Registration::getFinalGrade)
                        .setter(Registration::setFinalGrade))
                .addAttribute(Boolean.class, a -> a.name("active")
                        .getter(Registration::getActive)
                        .setter(Registration::setActive))
                .build();





}
