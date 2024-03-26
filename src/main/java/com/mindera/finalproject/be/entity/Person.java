package com.mindera.finalproject.be.entity;


import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import java.time.LocalDate;
import java.time.Period;


@DynamoDbBean
public abstract class Person {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String role;

    private String username;

    private LocalDate dateOfBirth;

    private int age;

    private String address;


    private int calcAge(){
        LocalDate currentDate = LocalDate.now();
        return Period.between(dateOfBirth, currentDate).getYears();
    }


}
