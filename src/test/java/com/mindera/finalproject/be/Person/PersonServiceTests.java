package com.mindera.finalproject.be.Person;

import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.impl.PersonServiceImpl;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@QuarkusTest
public class PersonServiceTests {
    private  String email = "test@example.com";
    private String firstName ="John";
    private String lastName = "Doe";
    private String role = "student";
    private String username = "JohnDoe";
    private LocalDate dateOfBirth = LocalDate.of(1999,10,10);
    private String address = "Portugal";
    private String cv = "www.example.com";
    private final String PERSON = "PERSON#";
    @Mock
    private DynamoDbTable<Person> mockPersonTable;

    @InjectMocks
    private PersonServiceImpl personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById() throws PersonNotFoundException {
        String id = PERSON + UUID.randomUUID();
        Person person = new Person();
        person.setPK(PERSON);
        person.setSK(id);
        Key key = Key.builder().partitionValue(PERSON).sortValue(id).build();

        when(mockPersonTable.getItem(key)).thenReturn(person);


        PersonPublicDto result = personService.getById(id);


        assertNotNull(result);
        assertEquals(id, result.id());
    }
    @Test
    void testCreate() {

        PersonCreateDto createDto = new PersonCreateDto(email,firstName,lastName,role,username,dateOfBirth,address,cv);

        PersonPublicDto result = personService.create(createDto);


        assertNotNull(result);
        assertEquals(createDto.email(), result.email());
        assertEquals(createDto.firstName(), result.firstName());
        assertEquals(createDto.lastName(), result.lastName());
        assertEquals(createDto.role(),result.role());
        assertEquals(createDto.username(),result.username());
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

        PersonCreateDto updateDto =  new PersonCreateDto(email,firstName,lastName,role,username,dateOfBirth,address,cv);



        when(mockPersonTable.getItem(key)).thenReturn(person);


        PersonPublicDto result = personService.update(id, updateDto);


        assertNotNull(result);
        assertEquals(updateDto.email(), result.email());
        assertEquals(updateDto.firstName(), result.firstName());
        assertEquals(updateDto.lastName(), result.lastName());
        assertEquals(updateDto.role(),result.role());
        assertEquals(updateDto.username(),result.username());
        assertEquals(updateDto.dateOfBirth(), result.dateOfBirth());
        assertEquals(updateDto.address(), result.address());
    }
    @Test
    void testDelete() {
        String id = PERSON + UUID.randomUUID();
        Person person = new Person();
        person.setPK(PERSON);
        person.setSK(id);
        Key key = Key.builder().partitionValue(PERSON).sortValue(id).build();

        when(mockPersonTable.getItem(key)).thenReturn(person);

        personService.delete(id);

        verify(mockPersonTable, times(1)).updateItem(person);
    }





}
