package com.mindera.finalproject.be.dto.project;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record ProjectCreateDto(

        @NotEmpty(message = "This field cannot be empty")
        @Pattern(regexp = "^[a-zA-Z_0-9~`'^ ]+$", message = "Invalid name")
        String name,

        List<String> studentIds,

        String courseId,

        @URL(message = "invalid url")
        String gitHubRepo


) {
}
