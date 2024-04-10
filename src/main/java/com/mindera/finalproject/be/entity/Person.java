package com.mindera.finalproject.be.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.LocalDate;
import java.util.Objects;


@DynamoDbBean
public class Person {

    private String PK;
    private String SK;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String username;
    private LocalDate dateOfBirth;
    private Integer age;
    private String address;
    private String cv;
    private boolean active;

    public Person() {
    }

    public Person(String email, String firstName, String lastName, String role, String username, LocalDate dateOfBirth, String address, String cv) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.cv = cv;
        this.active = true;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = {"GSIPK1", "GSIPK2"})
    public String getPK() {
        return PK;
    }

    public void setPK(String PK) {
        this.PK = PK;
    }

    @DynamoDbSortKey
    public String getSK() {
        return SK;
    }

    public void setSK(String SK) {
        this.SK = SK;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {"GSIPK2"})
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

    @DynamoDbSecondaryPartitionKey(indexNames = {"GSIPK1"})
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurriculum() {
        return cv;
    }

    public void setCurriculum(String cv) {
        this.cv = cv;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(PK, person.PK) && Objects.equals(SK, person.SK) && Objects.equals(email, person.email) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(role, person.role) && Objects.equals(username, person.username) && Objects.equals(dateOfBirth, person.dateOfBirth) && Objects.equals(age, person.age) && Objects.equals(address, person.address) && Objects.equals(cv, person.cv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(PK, SK, email, firstName, lastName, role, username, dateOfBirth, age, address, cv);
    }

    @Override
    public String toString() {
        return "Person{" +
                "PK='" + PK + '\'' +
                ", SK='" + SK + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", cv='" + cv + '\'' +
                '}';
    }
}
