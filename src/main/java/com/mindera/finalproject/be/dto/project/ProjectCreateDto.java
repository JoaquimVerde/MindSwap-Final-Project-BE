package com.mindera.finalproject.be.dto.project;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record ProjectCreateDto(

        @NotEmpty(message = "This field cannot be empty")
        @Pattern(regexp = "^[a-zA-Z_0-9~`'^ ]+$", message = "Invalid name")
        @Schema(description = "The project name", example = "Frontend Project 1")
        String name,
        @Schema(description = "The project student ids", example = "PERSON#asqwe-1234-asd,PERSON#asqwe-1234-asd")
        List<String> studentIds,
        @NotEmpty(message = "Course is mandatory")
        @Schema(description = "The project course id", example = "COURSE#asqwe-1234-asd")
        String courseId,

        @URL(message = "invalid url")
        @Schema(description = "The project gitHub repo", example = "https://github.com/user/repo")
        String gitHubRepo


) {
}
