package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Person;

import java.util.List;

public interface TeacherService {

    List<Person> findAll();

    PersonPublicDto get(Long id);

    PersonPublicDto add(PersonCreateDto personCreateDto);

    PersonPublicDto edit(Long id, PersonCreateDto personCreateDto);

    PersonPublicDto delete(Long id);
}
