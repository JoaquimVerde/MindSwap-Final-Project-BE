package com.mindera.finalproject.be.dto.project;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;

import java.util.List;

import static com.mindera.finalproject.be.messages.Messages.*;

public record ProjectCreateDto(

        @NotEmpty(message = NON_EMPTY_NAME)
        @Pattern(regexp = "^[a-zA-Z_0-9~`'^ ]+$", message = INVALID_NAME)
        @Schema(description = "The project name", example = "Frontend Project 1")
        String name,
        @Schema(description = "The project student ids", example = "PERSON#asqwe-1234-asd,PERSON#asqwe-1234-asd")
        List<String> studentIds,
        @NotEmpty(message = NON_EMPTY_COURSE)
        @Schema(description = "The project course id", example = "COURSE#asqwe-1234-asd")
        String courseId,

        @URL(message = INVALID_URL)
        @Schema(description = "The project gitHub repo", example = "https://github.com/user/repo")
        String gitHubRepo


) {
}
