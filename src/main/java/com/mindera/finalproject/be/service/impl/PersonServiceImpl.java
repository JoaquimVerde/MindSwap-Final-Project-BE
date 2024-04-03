package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.TableCreation.TableCreation;
import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.service.PersonService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;

@ApplicationScoped
public class PersonServiceImpl implements PersonService {

    private final String TABLE_NAME = "Person";
    private final String GSIPK = "GSIPK";
    @Inject
    TableCreation tableCreation;
    private DynamoDbTable<Person> personTable;

    @Inject
    void personEnchancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        personTable = dynamoEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Person.class));
    }

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
