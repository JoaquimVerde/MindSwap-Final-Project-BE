package com.mindera.finalproject.be.dto.person;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.jboss.resteasy.reactive.DateFormat;

import java.time.LocalDate;

public record PersonCreateDto(
        @NotBlank(message = "Invalid email")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email")
        String email,
        @NotBlank(message = "Invalid first name")
        @Pattern(regexp = "^[\\p{L}\\p{M}'\\s-]+$", message = "Invalid first name")
        String firstName,
        @NotBlank(message = "Invalid last name")
        @Pattern(regexp = "^[\\p{L}\\p{M}'\\s-]+$", message = "Invalid last name")
        String lastName,
        @NotBlank(message = "Invalid role")
        @Pattern(regexp = "^[\\p{L}\\p{M}'\\s-]+$", message = "Invalid role")
        String role,
        @NotBlank(message = "Invalid username")
        @Pattern(regexp = "^[a-zA-Z0-9](?:[a-zA-Z0-9_-]*[a-zA-Z0-9])?$", message = "Invalid username")
        String username,
        @NotNull(message = "Invalid date")
        @DateFormat
        LocalDate dateOfBirth,
        @NotBlank(message = "Invalid address")
        String address
) {
}
