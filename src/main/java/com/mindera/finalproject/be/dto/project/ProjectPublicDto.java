package com.mindera.finalproject.be.dto.project;

import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;

import java.util.List;

public record ProjectPublicDto(

        String id,
        String name,
        List<PersonPublicDto> students,
        CoursePublicDto course,
        String gitHubRepo,
        Integer grade

) {
}
