package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.TableCreation.TableCreation;
import com.mindera.finalproject.be.converter.PersonConverter;
import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.PersonService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PersonServiceImpl implements PersonService {

    private final String TABLE_NAME = "Person";
    private final String PERSON = "PERSON#";

    @Inject
    TableCreation tableCreation;
    private DynamoDbTable<Person> personTable;

    @Inject
    void personEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        personTable = dynamoEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Person.class));
    }

    @Override
    public List<PersonPublicDto> getAll() {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(k -> k.partitionValue(PERSON).sortValue(PERSON));
        SdkIterable<Page<Person>> persons = personTable.query(queryConditional);
        List<Person> personList = new ArrayList<>();
        persons.forEach(page -> personList.addAll(page.items()));
        return personList.stream().filter(Person::isActive).map(PersonConverter::fromEntityToPublicDto).toList();
    }

    @Override
    public PersonPublicDto getById(String id) throws PersonNotFoundException {
        Person person = findById(id);
        if (person == null) {
            throw new PersonNotFoundException("Person with id " + id + " not found");
        }
        return PersonConverter.fromEntityToPublicDto(person);

    }

    @Override
    public PersonPublicDto create(PersonCreateDto personCreateDto) {
        Person person = PersonConverter.fromCreateDtoToEntity(personCreateDto);
        person.setPK(PERSON);
        person.setSK(PERSON + UUID.randomUUID());
        personTable.putItem(person);
        return PersonConverter.fromEntityToPublicDto(person);
    }

    @Override
    public PersonPublicDto update(String id, PersonCreateDto personCreateDto) throws PersonNotFoundException {
        Person person = findById(id);
        if (person == null) {
            throw new PersonNotFoundException("Person with id " + id + " not found");
        }
        person.setEmail(personCreateDto.email());
        person.setFirstName(personCreateDto.firstName());
        person.setLastName(personCreateDto.lastName());
        person.setUsername(personCreateDto.username());
        person.setRole(personCreateDto.role());
        person.setDateOfBirth(personCreateDto.dateOfBirth());
        person.setAddress(personCreateDto.address());
        person.setCurriculum(personCreateDto.cv());

        personTable.updateItem(person);
        return PersonConverter.fromEntityToPublicDto(person);
    }

    @Override
    public void delete(String id) {
        Person person = personTable.getItem(Key.builder().partitionValue(PERSON).sortValue(id).build());
        person.setActive(false);
        personTable.updateItem(person);
    }

    @Override
    public Person findById(String id) {
        return personTable.getItem(Key.builder().partitionValue(PERSON).sortValue(id).build());
    }
}
