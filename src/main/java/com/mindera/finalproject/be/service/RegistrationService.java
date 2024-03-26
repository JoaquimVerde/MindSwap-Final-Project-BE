package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.entity.Registration;

import java.util.List;

public interface RegistrationService {
    Registration createRegistration(RegistrationCreateDto registrationCreateDto);
    List<RegistrationPublicDto> findAll();
    RegistrationPublicDto findByCompositeKey(String compositeKey);

    RegistrationCreateDto updateRegistration(String CompositeKey, RegistrationCreateDto registrationCreateDto);

    RegistrationPublicDto deleteRegistration(String compositeKey);

}
