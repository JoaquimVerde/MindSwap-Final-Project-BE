package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.exception.course.CourseNotFoundException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;

import java.util.List;

public interface RegistrationService {
    List<RegistrationPublicDto> getAll();

    RegistrationPublicDto getById(String id) throws PersonNotFoundException, CourseNotFoundException;

    RegistrationPublicDto create(RegistrationCreateDto registrationCreateDto) throws PersonNotFoundException, CourseNotFoundException;

    RegistrationPublicDto update(String id, RegistrationCreateDto registrationCreateDto) throws PersonNotFoundException, CourseNotFoundException;

    void delete(String id);
}
