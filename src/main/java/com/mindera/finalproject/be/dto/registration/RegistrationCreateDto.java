package com.mindera.finalproject.be.dto.registration;


import com.mindera.finalproject.be.enums.RegistrationStatus;
import com.mindera.finalproject.be.validator.EnumValidator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import static com.mindera.finalproject.be.messages.Messages.*;

public record RegistrationCreateDto(
        @NotEmpty(message = NON_EMPTY_PERSONID)
        String personId,

        @NotEmpty(message = NON_EMPTY_COURSEID)
        String courseId,

        @EnumValidator(enumClass = RegistrationStatus.class)
        String status,

        String finalGrade,

        @NotEmpty(message = NON_EMPTY_ABOUT_YOU)
        String aboutYou,

        @NotNull(message = NON_EMPTY_PREVKNOWLEDGE)
        Boolean prevKnowledge,

        @NotNull(message = NON_EMPTY_PREVEXPERIENCE)
        Boolean prevExperience
) {
}
