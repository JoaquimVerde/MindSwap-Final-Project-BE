package com.mindera.finalproject.be.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.LocalDate;

@DynamoDbBean
public class Staff extends Person{

    public Staff(Long id, String email, String firstName, String lastName, String role, String username, LocalDate dateOfBirth, int age, String address) {
        super(id, email, firstName, lastName, role, username, dateOfBirth, age, address);
    }
}
