package com.mindera.finalproject.be.dto.person;

import com.mindera.finalproject.be.enums.RoleStatus;
import com.mindera.finalproject.be.validator.EnumValidator;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.DateFormat;

import java.time.LocalDate;

import static com.mindera.finalproject.be.messages.Messages.*;

@RegisterForReflection
public record PersonCreateDto(
        @NotBlank(message = INVALID_EMAIL)
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = INVALID_EMAIL)
        @Schema(description = "The person email", example = "example@example.com")
        String email,
        @NotBlank(message = INVALID_FIRST_NAME)
        @Pattern(regexp = "^[\\p{L}\\p{M}'\\s-]+$", message = INVALID_FIRST_NAME)
        @Schema(description = "The person first name", example = "Joaquim")
        String firstName,
        @NotBlank(message = INVALID_LAST_NAME)
        @Pattern(regexp = "^[\\p{L}\\p{M}'\\s-]+$", message = INVALID_LAST_NAME)
        @Schema(description = "The person last name", example = "Verde")
        String lastName,
        @EnumValidator(enumClass = RoleStatus.class, message = INVALID_ROLE)
        @Schema(description = "The person role", example = "STUDENT")
        String role,
        @NotBlank(message = INVALID_USERNAME)
        @Pattern(regexp = "^[a-zA-Z0-9](?:[a-zA-Z0-9_-]*[a-zA-Z0-9])?$", message = INVALID_USERNAME)
        @Schema(description = "The person username", example = "badBoy")
        String username,
        @NotNull(message = INVALID_DATE)
        @Past(message = INVALID_DATE)
        @DateFormat
        @Schema(description = "The person date of birth", example = "1990-01-01")
        LocalDate dateOfBirth,
        @NotBlank(message = INVALID_ADDRESS)
        @Schema(description = "The person address", example = "Rua das Andorinhas, 123")
        String address,
        @NotBlank(message = INVALID_CURRICULUM)
        @Schema(description = "The person curriculum", example = "")  //TODO INSERT EXAMPLE
        String cv
) {

}
