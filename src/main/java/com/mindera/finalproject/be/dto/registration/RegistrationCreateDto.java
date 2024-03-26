package com.mindera.finalproject.be.dto.registration;


import jakarta.validation.constraints.NotNull;

public record RegistrationCreateDto(
        @NotNull
        Long personId,
        Long courseId,
        String status,
        String finalGrade,
        Boolean active
) {
}
