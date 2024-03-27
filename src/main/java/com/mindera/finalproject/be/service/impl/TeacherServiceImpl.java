package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.service.TeacherService;

import java.util.List;

public class TeacherServiceImpl implements TeacherService {

    @Override
    public List<Person> findAll() {
        return null;
    }

    @Override
    public PersonPublicDto get(Long id) {
        return null;
    }

    @Override
    public PersonPublicDto add(PersonCreateDto personCreateDto) {
        return null;
    }

    @Override
    public PersonPublicDto edit(Long id, PersonCreateDto personCreateDto) {
        return null;
    }

    @Override
    public PersonPublicDto delete(Long id) {
        return null;
    }
}
