package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.service.PersonService;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PersonServiceImpl implements PersonService {

    @Override
    public List<PersonPublicDto> getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public PersonPublicDto getById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public PersonPublicDto create(PersonCreateDto personCreateDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public PersonPublicDto update(String id, PersonCreateDto personCreateDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public PersonPublicDto delete(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }


}
