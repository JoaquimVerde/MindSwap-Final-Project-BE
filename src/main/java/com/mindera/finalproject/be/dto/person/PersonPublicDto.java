package com.mindera.finalproject.be.dto.person;

import java.time.LocalDate;

public record PersonPublicDto(
        String id,
        String email,
        String firstName,
        String lastName,
        String role,
        String username,
        LocalDate dateOfBirth,
        String address
) {
}
