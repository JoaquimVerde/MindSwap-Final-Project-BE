package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.exception.email.EmailGetTemplateException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;

import java.util.List;

public interface PersonService {

    List<PersonPublicDto> getAll();

    PersonPublicDto getById(String id) throws PersonNotFoundException;

    List<PersonPublicDto> getByRole(String role);

    PersonPublicDto create(PersonCreateDto personCreateDto) throws EmailGetTemplateException;

    PersonPublicDto update(String id, PersonCreateDto personCreateDto) throws PersonNotFoundException;

    void delete(String id) throws PersonNotFoundException;

    Person findById(String id) throws PersonNotFoundException;

    PersonPublicDto getByEmail(String email) throws PersonNotFoundException;

}
