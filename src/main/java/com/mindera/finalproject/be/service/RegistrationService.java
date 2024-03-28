package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.entity.Registration;

import java.util.List;

public interface RegistrationService {
    Registration create(RegistrationCreateDto registrationCreateDto);
    List<RegistrationPublicDto> getAll();
    RegistrationPublicDto getByCompositeKey(String compositeKey);

    RegistrationPublicDto update(String CompositeKey, RegistrationCreateDto registrationCreateDto);

    RegistrationPublicDto delete(String compositeKey);

}
