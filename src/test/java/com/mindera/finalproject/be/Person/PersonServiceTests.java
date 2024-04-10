package com.mindera.finalproject.be.Person;

import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTests {
    private final String email = "test@example.com";
    private final String firstName = "John";
    private final String lastName = "Doe";
    private final String role = "student";
    private final String username = "johndoe";
    private final LocalDate dateOfBirth = LocalDate.of(1999, 10, 10);
    private final String address = "Portugal";
    private final String cv = "www.example.com";
    private final String PERSON = "PERSON#";
    @Mock
    private DynamoDbTable<Person> personTable;

    @InjectMocks
    private PersonServiceImpl personService;


    @Test
    void testGetAll() {
        Person person1 = new Person();
        person1.setActive(true);
        Person person2 = new Person();
        person2.setActive(false);


        List<Person> mockPersons = new ArrayList<>();
        mockPersons.add(person1);
        mockPersons.add(person2);
        Page<Person> page1 = Page.create(mockPersons);
        SdkIterable<Page<Person>> persons = () -> List.of(page1).iterator();

        PageIterable<Person> pageIterable = PageIterable.create(persons);

        QueryConditional queryConditional = QueryConditional.sortBeginsWith(k -> k.partitionValue(PERSON).sortValue(PERSON));
        when(personTable.query(queryConditional)).thenReturn(pageIterable);

        List<PersonPublicDto> result = personService.getAll();


        assertEquals(1, result.size());
    }

    @Test
    void testGetById() throws PersonNotFoundException {
        String id = PERSON + UUID.randomUUID();
        Person person = new Person();
        person.setPK(PERSON);
        person.setSK(id);
        Key key = Key.builder().partitionValue(PERSON).sortValue(id).build();

        when(personTable.getItem(key)).thenReturn(person);


        PersonPublicDto result = personService.getById(id);


        assertNotNull(result);
        assertEquals(id, result.id());
    }

    @Test
    void testCreate() {

        PersonCreateDto createDto = new PersonCreateDto(email, firstName, lastName, role, username, dateOfBirth, address, cv);

        PersonPublicDto result = personService.create(createDto);


        assertNotNull(result);
        assertEquals(createDto.email(), result.email());
        assertEquals(createDto.firstName(), result.firstName());
        assertEquals(createDto.lastName(), result.lastName());
        assertEquals(createDto.role().toUpperCase(), result.role());
        assertEquals(createDto.username(), result.username());
        assertEquals(createDto.dateOfBirth(), result.dateOfBirth());
        assertEquals(createDto.address(), result.address());
    }

    @Test
    void testUpdate() throws PersonNotFoundException {

        String id = PERSON + UUID.randomUUID();
        Person person = new Person();
        person.setPK(PERSON);
        person.setSK(id);
        Key key = Key.builder().partitionValue(PERSON).sortValue(id).build();

        PersonCreateDto updateDto = new PersonCreateDto(email, firstName, lastName, role, username, dateOfBirth, address, cv);


        when(personTable.getItem(key)).thenReturn(person);


        PersonPublicDto result = personService.update(id, updateDto);


        assertNotNull(result);
        assertEquals(updateDto.email(), result.email());
        assertEquals(updateDto.firstName(), result.firstName());
        assertEquals(updateDto.lastName(), result.lastName());
        assertEquals(updateDto.role().toUpperCase(), result.role());
        assertEquals(updateDto.username(), result.username());
        assertEquals(updateDto.dateOfBirth(), result.dateOfBirth());
        assertEquals(updateDto.address(), result.address());
    }

    @Test
    void testDelete() throws PersonNotFoundException {
        String id = PERSON + UUID.randomUUID();
        Person person = new Person();
        person.setPK(PERSON);
        person.setSK(id);
        Key key = Key.builder().partitionValue(PERSON).sortValue(id).build();

        when(personTable.getItem(key)).thenReturn(person);

        personService.delete(id);

        verify(personTable, times(1)).updateItem(person);
    }

    @Test
    void testFindByIdNotFound() {
        String id = PERSON + UUID.randomUUID();

        when(personTable.getItem(any(Key.class))).thenReturn(null);

        assertThrows(PersonNotFoundException.class, () -> personService.findById(id));
    }

    @Test
    void testDeleteNotFound() {
        String id = PERSON + UUID.randomUUID();

        when(personTable.getItem(any(Key.class))).thenReturn(null);

        assertThrows(PersonNotFoundException.class, () -> personService.delete(id));
    }
}
