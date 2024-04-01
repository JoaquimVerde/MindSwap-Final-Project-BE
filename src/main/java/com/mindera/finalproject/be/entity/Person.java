package com.mindera.finalproject.be.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDate;
import java.time.Period;


@DynamoDbBean
public class Person {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String username;
    private LocalDate dateOfBirth;
    private int age;
    private String address;
    private String cv;


    public Person(String id, String email, String firstName, String lastName, String role, String username, LocalDate dateOfBirth, int age, String address, String cv) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.address = address;
        this.cv = cv;
    }

    public Person() {
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(int age) {
        LocalDate currentDate = LocalDate.now();
        this.age = Period.between(dateOfBirth, currentDate).getYears();
    }

    public String getCurriculum() {
        return cv;
    }

    public void setCurriculum(String cv) {
        this.cv = cv;
    }
}
