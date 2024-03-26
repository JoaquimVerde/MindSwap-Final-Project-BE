package com.mindera.finalproject.be.dto.registration;

public record RegistrationCreateDto(
        Long personId,
        Long courseId,
        String status,
        String finalGrade,
        Boolean active
) {
}
