package com.mindera.finalproject.be.dto.registration;

import com.mindera.finalproject.be.enums.RegistrationStatus;
import com.mindera.finalproject.be.validator.EnumValidator;

import static com.mindera.finalproject.be.messages.Messages.INVALID_REGISTRATION_STATUS;

public record RegistrationUpdateStatusDto(
        @EnumValidator(enumClass = RegistrationStatus.class, message = INVALID_REGISTRATION_STATUS)
        String status
) {
}
