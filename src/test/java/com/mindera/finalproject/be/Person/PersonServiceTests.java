package com.mindera.finalproject.be.Person;

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
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@QuarkusTest
public class PersonServiceTests {
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
        // Mocking data
        String id = PERSON + UUID.randomUUID();
        Person person = new Person();
        person.setPK(PERSON);
        person.setSK(id);
        person.setActive(true);
        Key key = Key.builder().partitionValue(PERSON).sortValue(id).build();

        // Mocking behavior of DynamoDbTable getItem method
        when(mockPersonTable.getItem(key)).thenReturn(person);

        // Calling the method under test
        PersonPublicDto result = personService.getById(id);

        // Verifying the result
        assertNotNull(result);
        assertEquals(id, result.id());
    }




}
