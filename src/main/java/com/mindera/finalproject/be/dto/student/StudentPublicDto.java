package com.mindera.finalproject.be.dto.student;

import java.time.LocalDate;

public record StudentPublicDto(
        String email,
        String firstName,
        String lastName,
        String role,
        String username,
        LocalDate dateOfBirth,
        String address,
        String curriculum


) {
}
