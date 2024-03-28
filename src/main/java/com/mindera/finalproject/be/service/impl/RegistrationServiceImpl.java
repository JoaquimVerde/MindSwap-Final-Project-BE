package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.entity.Registration;
import com.mindera.finalproject.be.repository.RegistrationRepository;
import com.mindera.finalproject.be.service.RegistrationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final ModelMapper modelMapper;

    @Inject
    public RegistrationServiceImpl(RegistrationRepository registrationRepository, ModelMapper modelMapper) {
        this.registrationRepository = registrationRepository;
        this.modelMapper = modelMapper;
    }

    //TODO: Implement Methods

    @Override
    public Registration create(RegistrationCreateDto registrationCreateDto) {
        Registration registration = modelMapper.map(registrationCreateDto, Registration.class);
        return registrationRepository.save(registration);
    }

    @Override
    public List<RegistrationPublicDto> getAll() {
        return registrationRepository.getAll().stream()
                .map(registration -> modelMapper.map(registration, RegistrationPublicDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RegistrationPublicDto getByCompositeKey(String compositeKey) {
        Registration registration =  registrationRepository.getByCompositeKey(compositeKey);
        return modelMapper.map(registration, RegistrationPublicDto.class);
    }

    @Override
    public RegistrationPublicDto update(String compositeKey, RegistrationCreateDto registrationCreateDto) {
        Registration existingRegistration = registrationRepository.getByCompositeKey(compositeKey);
        if (existingRegistration != null) {
            Registration updatedRegistration = modelMapper.map(registrationCreateDto, Registration.class);
            registrationRepository.update(updatedRegistration);
            return modelMapper.map(updatedRegistration, RegistrationPublicDto.class);
        } else {
            // Send exception "Not Found"
            return null;
        }
    }

    @Override
    public RegistrationPublicDto delete(String compositeKey) {
        Registration registration = registrationRepository.getByCompositeKey(compositeKey);
        registration.setActive(false);
        return modelMapper.map(registration, RegistrationPublicDto.class);
    }
}