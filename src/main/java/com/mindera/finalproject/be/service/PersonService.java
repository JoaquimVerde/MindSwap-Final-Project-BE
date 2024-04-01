package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;

import java.util.List;

public interface PersonService {

    List<PersonPublicDto> getAll();
    PersonPublicDto getById(String id);
    PersonPublicDto create(PersonCreateDto personCreateDto);
    PersonPublicDto update(String id, PersonCreateDto personCreateDto);
    PersonPublicDto delete(String id);
}
