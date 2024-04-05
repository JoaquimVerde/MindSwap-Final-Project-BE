package com.mindera.finalproject.be.dto.registration;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import static com.mindera.finalproject.be.messages.Messages.*;

public record RegistrationCreateDto(
        @NotEmpty(message = NON_EMPTY_PERSONID)
        String personId,

        @NotEmpty(message = NON_EMPTY_COURSEID)
        String courseId,

        @NotEmpty(message = NON_EMPTY_STATUS)
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
