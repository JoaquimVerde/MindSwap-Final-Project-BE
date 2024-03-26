package com.mindera.finalproject.be.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.io.File;
import java.time.LocalDate;

@DynamoDbBean
public class Student extends Person {
    private String curriculum;


    public Student(Long id, String email, String firstName, String lastName, String role, String username, LocalDate dateOfBirth, int age, String address, String curriculum) {
        super(id,email,firstName,lastName, role, username, dateOfBirth, age, address);
        this.curriculum = curriculum;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }
}
