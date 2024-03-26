package com.mindera.finalproject.be.dto.project;

import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Student;

import java.util.List;

public record ProjectPublicDto(

        Long id,
        List<Student> firstName, //change to studentPublicDto
        Course courseName, // change to coursePublicDto
        String name,
        String gitHubRepo

) {
}
