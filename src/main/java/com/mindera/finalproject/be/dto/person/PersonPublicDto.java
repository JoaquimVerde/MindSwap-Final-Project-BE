package com.mindera.finalproject.be.dto.person;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

public record PersonPublicDto(
        @Schema(description = "The person id", example = "PERSON#asdf-1234-1235")
        String id,
        @Schema(description = "The person email", example = "example@example.com")
        String email,
        @Schema(description = "The person first name", example = "Joaquim")
        String firstName,
        @Schema(description = "The person last name", example = "Verde")
        String lastName,
        @Schema(description = "The person role", example = "STUDENT")
        String role,
        @Schema(description = "The person username", example = "badBoy")
        String username,
        @Schema(description = "The person date of birth", example = "1990-01-01")
        LocalDate dateOfBirth,
        @Schema(description = "The person address", example = "Rua das Andorinhas, 123")
        String address
) {
}
