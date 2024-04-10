package com.mindera.finalproject.be.dto.registration;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import static com.mindera.finalproject.be.messages.Messages.INVALID_GRADE;
import static com.mindera.finalproject.be.messages.Messages.NON_EMPTY_GRADE;

public record RegistrationUpdateGradeDto(
        @Min(value = 0, message = INVALID_GRADE)
        @Max(value  =20, message = INVALID_GRADE)
        @Schema(example = "10")
        Integer grade
) {
}
