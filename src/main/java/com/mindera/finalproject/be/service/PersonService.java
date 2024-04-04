package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;

import java.util.List;

public interface PersonService {

    List<PersonPublicDto> getAll();

    PersonPublicDto getById(String id) throws PersonNotFoundException;

    List<PersonPublicDto> getByRole(String role);

    PersonPublicDto create(PersonCreateDto personCreateDto);

    PersonPublicDto update(String id, PersonCreateDto personCreateDto) throws PersonNotFoundException;

    void delete(String id);

    Person findById(String id) throws PersonNotFoundException;

}
