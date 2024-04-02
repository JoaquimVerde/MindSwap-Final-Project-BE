package com.mindera.finalproject.be.dto.project;

import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Course;
import java.util.List;

public record ProjectPublicDto(

        String id,
        String name,
        List<String> studentIds, //change to studentPublicDto
        String courseId, // change to coursePublicDto
        String gitHubRepo,
        Integer grade

) {
}
