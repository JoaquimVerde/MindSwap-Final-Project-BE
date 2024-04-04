package com.mindera.finalproject.be.dto.course;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

public record CourseCreateDto(

        @NotEmpty(message = "Name is mandatory")
        @Pattern(regexp = "^[\\p{L}- ]+$", message = "Invalid name")
        @Schema(description = "The course name", example = "Frontend")
        String name,

        @NotNull(message = "Edition is mandatory")
        @Positive(message = "Edition must be bigger than 0")
        @Schema(description = "The course edition", example = "1")
        Integer edition,

        @Schema(description = "The course teacher id", example = "TEACHER#asqwe-1234-asd")
        String teacherId,

        @NotEmpty(message = "Syllabus is mandatory")
        @Schema(description = "The course syllabus", example = "HTML, CSS, JavaScript")
        String syllabus,

        @NotEmpty(message = "Program is mandatory")
        @Schema(description = "The course program", example = "Frontend")
        String program,

        @NotEmpty(message = "Schedule is mandatory")
        @Schema(description = "The course schedule", example = "Part-time")
        String schedule,

        @NotNull(message = "Price is mandatory")
        @Positive(message = "Price can't be negative")
        @Schema(description = "The course price", example = "900.00")
        BigDecimal price,

        @NotNull(message = "Duration is mandatory")
        @Positive(message = "Duration be bigger than 0")
        @Schema(description = "The course duration", example = "30")
        Integer duration,

        @NotEmpty(message = "Location is mandatory")
        @Schema(description = "The course location", example = "Porto")
        String location
){
}
