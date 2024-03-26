package com.mindera.finalproject.be.entity;

import java.time.LocalDate;
import java.time.Period;

public abstract class Account {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String username;

    private LocalDate dateOfBirth;

    private int age;

    private String address;


    private int calcAge(){
        LocalDate currentDate = LocalDate.now();
        return Period.between(dateOfBirth, currentDate).getYears();
    }

}
