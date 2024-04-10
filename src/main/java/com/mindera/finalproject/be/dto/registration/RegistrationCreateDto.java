package com.mindera.finalproject.be.dto.registration;


import com.mindera.finalproject.be.enums.RegistrationStatus;
import com.mindera.finalproject.be.validator.EnumValidator;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import static com.mindera.finalproject.be.messages.Messages.*;

@RegisterForReflection
public record RegistrationCreateDto(
        @NotEmpty(message = NON_EMPTY_PERSONID)
        String personId,

        @NotEmpty(message = NON_EMPTY_COURSEID)
        String courseId,

        @EnumValidator(enumClass = RegistrationStatus.class, message = INVALID_REGISTRATION_STATUS)
        String status,

        @Min(value = 0, message = INVALID_GRADE)
        @Max(value = 20, message = INVALID_GRADE)
        @Schema(example = "10")
        Integer finalGrade,

        @NotEmpty(message = NON_EMPTY_ABOUT_YOU)
        String aboutYou,

        @NotNull(message = NON_EMPTY_PREVKNOWLEDGE)
        Boolean prevKnowledge,

        @NotNull(message = NON_EMPTY_PREVEXPERIENCE)
        Boolean prevExperience
) {
}
