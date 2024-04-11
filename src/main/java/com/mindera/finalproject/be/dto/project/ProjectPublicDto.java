package com.mindera.finalproject.be.dto.project;

import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@RegisterForReflection
public record ProjectPublicDto(

        @Schema(description = "The project id", example = "PROJECT#asdf-1234-1235")
        String id,
        @Schema(description = "The project name", example = "Frontend Project 1")
        String name,
        @Schema(description = "The project student ids", example = "PERSON#asqwe-1234-asd,PERSON#asqwe-1234-asd")
        List<PersonPublicDto> students,
        @Schema(description = "The project course id", example = "COURSE#asqwe-1234-asd")
        CoursePublicDto course,
        @Schema(description = "The project gitHub repo", example = "https://github.com/user/repo")
        String gitHubRepo,
        @Schema(description = "The project grade", example = "10")
        Integer grade

) {
}
