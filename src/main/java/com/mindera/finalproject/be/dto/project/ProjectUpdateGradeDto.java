package com.mindera.finalproject.be.dto.project;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import static com.mindera.finalproject.be.messages.Messages.NON_EMPTY_GRADE;

@RegisterForReflection
public record ProjectUpdateGradeDto(

        @NotEmpty(message = NON_EMPTY_GRADE)
        @Pattern(regexp = "^(0?|1?\\d|20)$", message = "Grade must be a number between 0 and 20")
        @Schema(example = "10")
        Integer grade
) {
}
