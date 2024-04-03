package com.mindera.finalproject.be.dto.registration;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegistrationCreateDto(
        @NotEmpty(message = "Person id is required")
        String personId,

        @NotEmpty(message = "Course id is required")
        String courseId,

        @NotEmpty(message = "Registration status is required")
        String status,

        String finalGrade,

        @NotEmpty(message = "About you is required")
        String aboutYou,

        @NotNull(message = "Previous knowledge status is required")
        Boolean prevKnowledge,

        @NotNull(message = "Previous experience status is required")
        Boolean prevExperience
) {
}
