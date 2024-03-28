package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.service.TeacherService;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TeacherServiceImpl implements TeacherService {

    @Override
    public List<PersonPublicDto> findAll() {
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
