package com.mindera.finalproject.be.dto.registration;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegistrationCreateDto(
        @NotEmpty(message = "Person id is required")
        Long personId,

        @NotEmpty(message = "Course id is required")
        Long courseId,

        @NotEmpty(message = "Registration status is required")
        String status,

        String finalGrade,

        @NotEmpty(message = "Active status is required")
        Boolean active
) {
}
