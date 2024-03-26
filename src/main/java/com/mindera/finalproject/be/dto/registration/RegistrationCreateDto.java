package com.mindera.finalproject.be.dto.registration;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrationCreateDto(
        @NotNull
        Long personId,

        @NotNull
        Long courseId,

        @NotBlank
        String status,

        String finalGrade,

        @NotBlank
        Boolean active
) {
}
