package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.dto.registration.RegistrationUpdateGradeDto;
import com.mindera.finalproject.be.dto.registration.RegistrationUpdateStatusDto;
import com.mindera.finalproject.be.exception.registration.RegistrationAlreadyExistsException;
import com.mindera.finalproject.be.exception.course.CourseNotFoundException;
import com.mindera.finalproject.be.exception.registration.RegistrationNotFoundException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;

import java.util.List;

public interface RegistrationService {
    List<RegistrationPublicDto> getAll(Integer page, Integer limit);

    RegistrationPublicDto getById(String id) throws PersonNotFoundException, CourseNotFoundException, RegistrationNotFoundException;

    RegistrationPublicDto create(RegistrationCreateDto registrationCreateDto)
            throws PersonNotFoundException, CourseNotFoundException, RegistrationAlreadyExistsException;

    RegistrationPublicDto update(String id, RegistrationCreateDto registrationCreateDto)
            throws PersonNotFoundException, CourseNotFoundException;

    void delete(String id);

    List<RegistrationPublicDto> getRegistrationsByPerson(String personId);

    List<RegistrationPublicDto> getRegistrationsByCourse(String courseId);

    RegistrationPublicDto updateStatus(String id, RegistrationUpdateStatusDto registrationUpdate) throws PersonNotFoundException, CourseNotFoundException, RegistrationNotFoundException;

    RegistrationPublicDto updateGrade(String id, RegistrationUpdateGradeDto registrationUpdate) throws PersonNotFoundException, CourseNotFoundException;


}
