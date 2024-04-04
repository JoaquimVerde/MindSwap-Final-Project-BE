package com.mindera.finalproject.be.dto.project;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record ProjectUpdateGradeDto(

        @NotEmpty(message = "Grade is required")
        @Pattern(regexp = "^(0?|1?\\d|20)$", message = "Grade must be a number between 0 and 20")
        @Schema(example = "10")
        Integer grade
) {
}
