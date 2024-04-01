package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.entity.Registration;

import java.util.List;

public interface RegistrationService {
    List<RegistrationPublicDto> getAll();
    /* RegistrationPublicDto getByCompositeKey(String compositeKey); */
    RegistrationPublicDto getById(String id);
    Registration create(RegistrationCreateDto registrationCreateDto);
    RegistrationPublicDto update(String id, RegistrationCreateDto registrationCreateDto);
    void delete(String id);
}
