package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.converter.PersonConverter;
import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.email.Email;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.exception.email.EmailGetTemplateException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.PersonService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mindera.finalproject.be.messages.Messages.PERSON_NOT_FOUND;

@ApplicationScoped
public class PersonServiceImpl implements PersonService {

    private final String TABLE_NAME = "Person";
    private final String PERSON = "PERSON#";
    private final String GSIPK1 = "GSIPK1";
    private final String GSIPK2 = "GSIPK2";
    @Inject
    Email email;
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
            throw new PersonNotFoundException(PERSON_NOT_FOUND + id);
        }
        return PersonConverter.fromEntityToPublicDto(person);
    }

    @Override
    public List<PersonPublicDto> getByRole(String role) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(role));
        DynamoDbIndex<Person> personIndex = personTable.index(GSIPK1);
        SdkIterable<Page<Person>> persons = personIndex.query(queryConditional);
        List<Person> personList = new ArrayList<>();
        persons.forEach(page -> personList.addAll(page.items()));
        return personList.stream().filter(Person::isActive).map(PersonConverter::fromEntityToPublicDto).toList();
    }

    @Override
    public PersonPublicDto getByEmail(String email) throws PersonNotFoundException {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(k -> k.partitionValue(email));
        DynamoDbIndex<Person> personIndex = personTable.index(GSIPK2);
        SdkIterable<Page<Person>> persons = personIndex.query(queryConditional);
        List<Person> personList = new ArrayList<>();
        persons.forEach(page -> personList.addAll(page.items()));
        return personList.stream().filter(Person::isActive).map(PersonConverter::fromEntityToPublicDto).findFirst().orElseThrow(() -> new PersonNotFoundException("email"));
    }

    @Override
    public PersonPublicDto create(PersonCreateDto personCreateDto) throws EmailGetTemplateException {
        Person person = PersonConverter.fromCreateDtoToEntity(personCreateDto);
        person.setPK(PERSON);
        person.setSK(PERSON + personCreateDto.id());
        person.setRole(personCreateDto.role().toUpperCase());
        personTable.putItem(person);
       if (person.getRole().equals("STUDENT")) {
            email.sendWelcomeEmail(person);
        }
        return PersonConverter.fromEntityToPublicDto(person);
    }

    @Override
    public PersonPublicDto update(String id, PersonCreateDto personCreateDto) throws PersonNotFoundException {
        Person person = findById(id);
        if (person == null) {
            throw new PersonNotFoundException(PERSON_NOT_FOUND + id);
        }
        person.setEmail(personCreateDto.email());
        person.setFirstName(personCreateDto.firstName());
        person.setLastName(personCreateDto.lastName());
        person.setUsername(personCreateDto.username());
        person.setRole(personCreateDto.role().toUpperCase());
        person.setDateOfBirth(personCreateDto.dateOfBirth());
        person.setAddress(personCreateDto.address());
        person.setCurriculum(personCreateDto.cv());

        personTable.updateItem(person);
        return PersonConverter.fromEntityToPublicDto(person);
    }

    @Override
    public void delete(String id) throws PersonNotFoundException {
        Person person = findById(id);
        person.setActive(false);
        personTable.updateItem(person);
    }

    @Override
    public Person findById(String id) throws PersonNotFoundException {
        Person person = personTable.getItem(Key.builder().partitionValue(PERSON).sortValue(id).build());
        if (person == null) {
            throw new PersonNotFoundException(PERSON_NOT_FOUND + id);
        }
        return person;
    }
}
