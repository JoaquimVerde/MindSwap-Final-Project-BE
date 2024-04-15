package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.dto.registration.RegistrationUpdateGradeDto;
import com.mindera.finalproject.be.dto.registration.RegistrationUpdateStatusDto;
import com.mindera.finalproject.be.exception.course.MaxNumberOfStudentsException;
import com.mindera.finalproject.be.exception.email.EmailGetTemplateException;
import com.mindera.finalproject.be.exception.pdf.PdfCreateException;
import com.mindera.finalproject.be.exception.registration.RegistrationAlreadyExistsException;
import com.mindera.finalproject.be.exception.course.CourseNotFoundException;
import com.mindera.finalproject.be.exception.registration.RegistrationNotFoundException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;

import java.util.List;

public interface RegistrationService {
    List<RegistrationPublicDto> getAll(Integer page, Integer limit);

    RegistrationPublicDto getById(String id) throws PersonNotFoundException, CourseNotFoundException, RegistrationNotFoundException;

    RegistrationPublicDto create(RegistrationCreateDto registrationCreateDto)
            throws PersonNotFoundException, CourseNotFoundException, RegistrationAlreadyExistsException, EmailGetTemplateException;

    void delete(String id) throws RegistrationNotFoundException;

    List<RegistrationPublicDto> getRegistrationsByPerson(String personId, Integer page, Integer limit);

    List<RegistrationPublicDto> getRegistrationsByCourse(String courseId, Integer page, Integer limit);

    RegistrationPublicDto updateStatus(String id, RegistrationUpdateStatusDto registrationUpdate) throws PersonNotFoundException, CourseNotFoundException, RegistrationNotFoundException, MaxNumberOfStudentsException, EmailGetTemplateException, PdfCreateException;

    RegistrationPublicDto updateGrade(String id, RegistrationUpdateGradeDto registrationUpdate) throws PersonNotFoundException, CourseNotFoundException, RegistrationNotFoundException;


}
