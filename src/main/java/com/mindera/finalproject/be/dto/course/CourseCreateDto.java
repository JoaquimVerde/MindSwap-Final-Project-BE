package com.mindera.finalproject.be.dto.course;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Map;

public record CourseCreateDto(

        @NotEmpty(message = "Name is mandatory")
        @Pattern(regexp = "^[\\p{L} ]+$", message = "Name can only contain letters")
        @Schema(description = "The course name", example = "Frontend")
        String name,

        @Positive(message = "Edition is mandatory")
        @Schema(description = "The course edition", example = "1")
        Integer edition,

        @NotEmpty(message = "Teacher id is mandatory")
        @Pattern(regexp = "^[0-9]+$", message = "Teacher id can only contain numbers")
        @Schema(description = "The course teacher id", example = "1")
        String teacherId,

        @NotEmpty(message = "Syllabus is mandatory")
        @Schema(description = "The course syllabus", example = "HTML, CSS, JavaScript")
        String syllabus,

        @NotEmpty(message = "Program is mandatory")
        @Schema(description = "The course program", example = "Frontend")
        String program,

        @NotEmpty(message = "Schedule is mandatory")
        @Schema(description = "The course schedule", example = "{monday=10-18, tuesday=14-18}")
        Map<String, String> schedule,

        @Positive(message = "Price is mandatory")
        @Schema(description = "The course price", example = "900.00")
        BigDecimal price,

        @Positive(message = "Duration is mandatory")
        @Schema(description = "The course duration", example = "30")
        Integer duration,

        @NotEmpty(message = "Location is mandatory")
        @Schema(description = "The course location", example = "Porto")
        String location
){
}
