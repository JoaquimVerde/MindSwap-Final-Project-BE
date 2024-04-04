package com.mindera.finalproject.be.Person;

import com.mindera.finalproject.be.converter.PersonConverter;
import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.entity.Person;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.time.LocalDate;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class PersonControllerTests {

    @Inject
    private DynamoDbEnhancedClient dynamoEnhancedClient;
    private DynamoDbTable<Person> personTable;


    @BeforeEach
    public void setUp() {
        personTable = dynamoEnhancedClient.table("Person", TableSchema.fromBean(Person.class));
        personTable.createTable();
        }

        @AfterEach
        public void tearDown() {
            personTable.deleteTable();
        }

        public String createPerson() {
            PersonCreateDto newPerson = new PersonCreateDto("peter@email.com", "joe", "doe", "student", "peter", LocalDate.of(2001, 10, 10), "address", "cv");

            return given()
                    .body(newPerson)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .when().post("/api/v1/persons")
                    .then()
                    .statusCode(201)
                    .extract().jsonPath().getString("id");
    }
    @Test
    void contextLoads() {
    }
    @Test
    public void testGetAllPersons() {
        given()
                .when().get("/api/v1/persons")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("$", hasSize(greaterThanOrEqualTo(0)));
    }


    @Test
    public void testGetById() {

        String personId = createPerson();

        given()
                .pathParam("id", personId)
                .when()
                .get("/api/v1/persons/{id}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("id", equalTo(personId));
    }
    @Test
    public void testGetByIdNotFound() {

        String id = "XXXXXXX"; //non-existing ID

    given()
            .pathParam("id", id)
            .when().get("/api/v1/persons/{id}")
            .then()
            .statusCode(Response.Status.NOT_FOUND.getStatusCode());
}



    @Test
    public void testCreate() {
        PersonCreateDto personCreateDto = new PersonCreateDto("peter@email.com", "peter", "doe", "admin", "peter", LocalDate.of(1999, 10, 10), "address", "cv");
        given()
                .contentType(ContentType.JSON)
                .body(personCreateDto)
                .when().post("/api/v1/persons")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }


    @Test
    public void testUpdate() {
        String personId = createPerson();

        PersonCreateDto personCreateDto = new PersonCreateDto("peter@email.com", "peter", "doe", "admin", "peter", LocalDate.of(2000, 10, 10), "address", "cv");

        given()
                .pathParam("id", personId)
                .contentType(ContentType.JSON)
                .body(personCreateDto)
                .when().put("/api/v1/persons/{id}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testUpdateNotFound() {

        String id = "XXXXXXXXX"; //non-existing ID
        PersonCreateDto personCreateDto = new PersonCreateDto("peter@email.com", "joe", "doe", "admin", "peter", LocalDate.of(2000, 10, 10), "address", "cv");
        given()
                .pathParam("id", id)
                .contentType(ContentType.JSON)
                .body(personCreateDto)
                .when().put("/api/v1/persons/{id}")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testDelete() {

        String personId = createPerson();
        given()
                .pathParam("id", personId)
                .when().delete("/api/v1/persons/{id}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testDeleteNotFound() {

        String id = "XXXXXXXXX"; //non-existing ID
        given()
                .pathParam("id", id)
                .when()
                .delete("/api/v1/persons/{id}")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}


