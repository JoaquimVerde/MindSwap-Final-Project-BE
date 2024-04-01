package com.mindera.finalproject.be.dto.project;

import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Course;
import java.util.List;

public record ProjectPublicDto(

        String id,
        List<PersonPublicDto> firstName, //change to studentPublicDto
        Course courseName, // change to coursePublicDto
        String name,
        String gitHubRepo

) {
}
