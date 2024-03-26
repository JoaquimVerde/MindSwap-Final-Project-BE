package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.entity.Registration;
import com.mindera.finalproject.be.service.RegistrationService;

import java.util.List;

public class RegistrationServiceImpl implements RegistrationService {

    //TODO: Implement Methods

    @Override
    public Registration createRegistration(RegistrationCreateDto registrationCreateDto) {
        return null;
    }

    @Override
    public List<RegistrationPublicDto> findAll() {
        return null;
    }

    @Override
    public RegistrationPublicDto findByCompositeKey(String compositeKey) {
        return null;
    }

    @Override
    public RegistrationCreateDto updateRegistration(String CompositeKey, RegistrationCreateDto registrationCreateDto) {
        return null;
    }

    @Override
    public RegistrationPublicDto deleteRegistration(String compositeKey) {
        return null;
    }
}
