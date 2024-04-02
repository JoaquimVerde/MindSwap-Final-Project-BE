package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.converter.PersonConverter;
import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.service.PersonService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class PersonServiceImpl implements PersonService {
    public static final String PERSON_TABLE_NAME = "Person";

    private DynamoDbTable<Person> personTable;

    @Inject
    void courseEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        personTable = dynamoEnhancedClient.table(PERSON_TABLE_NAME, TableSchema.fromBean(Person.class));
    }

    @Override
    public List<PersonPublicDto> getAll() {
        return PersonConverter.fromEntityListToDtoList(personTable.scan().items().stream().toList());

    }

    @Override
    public PersonPublicDto getById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return PersonConverter.fromEntityToDto(personTable.getItem(key));

    }

    @Override
    public PersonPublicDto create(PersonCreateDto personCreateDto) {
        Person person = PersonConverter.fromDtoToEntity(personCreateDto);
        personTable.putItem(person);
        return PersonConverter.fromEntityToDto(person);
    }

    @Override
    public PersonPublicDto update(String id, PersonCreateDto personCreateDto) {
        Person person = PersonConverter.fromDtoToEntity(personCreateDto);
        return PersonConverter.fromEntityToDto(personTable.updateItem(person));
    }

    @Override
    public PersonPublicDto delete(String id) {
        Key key = Key.builder().partitionValue(id).build();
      return PersonConverter.fromEntityToDto(personTable.deleteItem(key));
    }


}
