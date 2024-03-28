package com.mindera.finalproject.be.dto.course;

import com.mindera.finalproject.be.entity.Person;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Map;

public record CoursePublicDto(

        @Schema(description = "The course id", example = "1")
        Long id,
        @Schema(description = "The course name", example = "Frontend")
        String name,
        @Schema(description = "The course edition", example = "1")
        Integer edition,
        @Schema(description = "The course teacher", example = "John Doe")
        Person teacher,
        @Schema(description = "The course syllabus", example = "HTML, CSS, JavaScript")
        String syllabus,
        @Schema(description = "The course program", example = "Frontend")
        String program,
        @Schema(description = "The course schedule", example = "{monday=10-18, tuesday=14-18}")
        Map<String, String> schedule,
        @Schema(description = "The course price", example = "900.00")
        BigDecimal price,
        @Schema(description = "The course duration", example = "30")
        Integer duration,
        @Schema(description = "The course location", example = "Porto")
        String location
) {
}