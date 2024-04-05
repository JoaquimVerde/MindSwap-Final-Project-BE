package com.mindera.finalproject.be.dto.person;

import com.mindera.finalproject.be.enums.RoleStatus;
import com.mindera.finalproject.be.validator.EnumValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.DateFormat;

import java.time.LocalDate;

public record PersonCreateDto(
        @NotBlank(message = "Invalid email")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email")
        @Schema(description = "The person email", example = "example@example.com")
        String email,
        @NotBlank(message = "Invalid first name")
        @Pattern(regexp = "^[\\p{L}\\p{M}'\\s-]+$", message = "Invalid first name")
        @Schema(description = "The person first name", example = "Joaquim")
        String firstName,
        @NotBlank(message = "Invalid last name")
        @Pattern(regexp = "^[\\p{L}\\p{M}'\\s-]+$", message = "Invalid last name")
        @Schema(description = "The person last name", example = "Verde")
        String lastName,
        @NotBlank(message = "Invalid role")
        @EnumValidator(enumClass = RoleStatus.class, message = "Invalid role")
        @Schema(description = "The person role", example = "STUDENT")
        String role,
        @NotBlank(message = "Invalid username")
        @Pattern(regexp = "^[a-zA-Z0-9](?:[a-zA-Z0-9_-]*[a-zA-Z0-9])?$", message = "Invalid username")
        @Schema(description = "The person username", example = "badBoy")
        String username,
        @NotNull(message = "Invalid date")
        @DateFormat
        @Schema(description = "The person date of birth", example = "1990-01-01")
        LocalDate dateOfBirth,
        @NotBlank(message = "Invalid address")
        @Schema(description = "The person address", example = "Rua das Andorinhas, 123")
        String address,
        @NotBlank(message = "Invalid curriculum")
        @Schema(description = "The person curriculum", example = "")  //TODO INSERT EXAMPLE
        String cv
) {
}
