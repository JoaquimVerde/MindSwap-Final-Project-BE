package com.mindera.finalproject.be.dto.course;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

import static com.mindera.finalproject.be.messages.Messages.*;

@RegisterForReflection
public record CourseCreateDto(

        @NotEmpty(message = NON_EMPTY_NAME)
        @Pattern(regexp = "^[\\p{L}- ]+$", message = INVALID_NAME)
        @Schema(description = "The course name", example = "Frontend")
        String name,

        @NotNull(message = NON_EMPTY_EDITION)
        @Positive(message = INVALID_EDITION)
        @Schema(description = "The course edition", example = "1")
        Integer edition,

        @Schema(description = "The course teacher id", example = "TEACHER#asqwe-1234-asd")
        String teacherId,

        @NotEmpty(message = NON_EMPTY_SYLLABUS)
        @Schema(description = "The course syllabus", example = "HTML, CSS, JavaScript")
        String syllabus,

        @NotEmpty(message = NON_EMPTY_PROGRAM)
        @Schema(description = "The course program", example = "Frontend")
        String program,

        @NotEmpty(message = NON_EMPTY_SCHEDULE)
        @Schema(description = "The course schedule", example = "Part-time")
        String schedule,

        @NotNull(message = NON_EMPTY_PRICE)
        @Positive(message = NON_NEGATIVE_PRICE)
        @Schema(description = "The course price", example = "900.00")
        BigDecimal price,

        @NotNull(message = NON_EMPTY_DURATION)
        @Positive(message = NON_NEGATIVE_DURATION)
        @Schema(description = "The course duration", example = "30")
        Integer duration,

        @NotEmpty(message = NON_EMPTY_LOCATION)
        @Schema(description = "The course location", example = "Porto")
        String location
) {
}
