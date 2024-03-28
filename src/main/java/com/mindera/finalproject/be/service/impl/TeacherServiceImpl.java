package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.repository.TeacherRepository;
import com.mindera.finalproject.be.service.TeacherService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TeacherServiceImpl implements TeacherService {

    @Inject
    TeacherRepository teacherRepository;

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
