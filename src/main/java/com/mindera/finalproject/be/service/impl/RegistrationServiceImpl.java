package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.entity.Registration;
import com.mindera.finalproject.be.repository.RegistrationRepository;
import com.mindera.finalproject.be.service.RegistrationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;

    @Inject
    public RegistrationServiceImpl(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    //TODO: Implement Methods

    @Override
    public Registration create(RegistrationCreateDto registrationCreateDto) {
        Registration registration = null;
        return registrationRepository.save(registration);
    }

    @Override
    public List<RegistrationPublicDto> getAll() {
        return null;
    }

    @Override
    public RegistrationPublicDto getByCompositeKey(String compositeKey) {
        Registration registration = registrationRepository.getByCompositeKey(compositeKey);
        return null;
    }

    @Override
    public RegistrationPublicDto update(String compositeKey, RegistrationCreateDto registrationCreateDto) {
        Registration existingRegistration = registrationRepository.getByCompositeKey(compositeKey);
        if (existingRegistration != null) {
            Registration updatedRegistration = null;
            registrationRepository.update(updatedRegistration);
            return null;
        } else {
            // Send exception "Not Found"
            return null;
        }
    }

    @Override
    public RegistrationPublicDto delete(String compositeKey) {
        Registration registration = registrationRepository.getByCompositeKey(compositeKey);
        registration.setActive(false);
        return null;
    }
}