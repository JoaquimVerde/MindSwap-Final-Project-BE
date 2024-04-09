package com.mindera.finalproject.be.Registration;

import com.mindera.finalproject.be.aspect.Error;
import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Registration;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RegistrationControllerTests {

    private final String URL = "/api/v1/registration";

    @Inject
    DynamoDbEnhancedClient enhancedClient;

    private DynamoDbTable<Person> personTable;
    private DynamoDbTable<Course> courseTable;
    private DynamoDbTable<Registration> registrationTable;

    @BeforeEach
    void setUp() {
        personTable = enhancedClient.table("Person", TableSchema.fromBean(Person.class));
        courseTable = enhancedClient.table("Course", TableSchema.fromBean(Course.class));
        registrationTable = enhancedClient.table("Registration", TableSchema.fromBean(Registration.class));

        try {
            courseTable.createTable();
            personTable.createTable();
            registrationTable.createTable();
            Thread.sleep(100);
        } catch (Exception e) {
            courseTable.deleteTable();
            personTable.deleteTable();
            registrationTable.deleteTable();
            setUp();
        }
    }

    @AfterEach
    void tearDown() {
        courseTable.deleteTable();
        personTable.deleteTable();
        registrationTable.deleteTable();
    }

    public String createPerson(String role) {
        PersonCreateDto person = new PersonCreateDto("example@email.com", "firstName", "lastName", role, "password",
                LocalDate.of(1990, 1, 1), "city", "phone");

        return given()
                .body(person)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post("/api/v1/persons")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    public String createCourse(String personId) {
        CourseCreateDto course = new CourseCreateDto("Test Course", 1, personId, "Syllabus", "Program",
                "Schedule",
                new BigDecimal(100), 10, "Porto");

        return given()
                .body(course)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post("/api/v1/courses")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    public String createRegistration(String studentId, String courseId) {
        RegistrationCreateDto registration = new RegistrationCreateDto(studentId, courseId, "Pending", "10",
                "about", true, true);

        return given()
                .body(registration)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(URL)
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    @Test
    void testGetAllRegistrationsReturns200() {
        given()
                .when().get(URL)
                .then()
                .statusCode(200);
    }

    @Test
    void testGetAllRegistrationsReturnsEmptyList() {
        given()
                .when().get(URL)
                .then()
                .body("size()", equalTo(0));
    }

    @Test
    void testGetAllRegistrationsWith5Registrations() {
        int amount = 5;
        for (int i = 0; i < amount; i++) {
            createRegistration(createPerson("Student"), createCourse(createPerson("Teacher")));
        }

        given()
                .when().get(URL)
                .then()
                .body("size()", equalTo(5));
    }

    @Test
    void testCreateRegistration() {
        String studentId = createPerson("Student");
        String courseId = createCourse(createPerson("Teacher"));

        RegistrationCreateDto registration = new RegistrationCreateDto(studentId, courseId, "Pending", "10",
                "about", true, true);

        RegistrationPublicDto response = given()
                .body(registration)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(URL)
                .then()
                .statusCode(201)
                .extract().as(RegistrationPublicDto.class);

        assertNotNull(response.id());
        assertEquals(registration.personId(), response.student().id());
        assertEquals(registration.courseId(), response.course().id());
        assertEquals(registration.status(), response.status());
        assertEquals(registration.finalGrade(), response.finalGrade());
        assertEquals(registration.aboutYou(), response.aboutYou());
        assertEquals(registration.prevKnowledge(), response.prevKnowledge());
        assertEquals(registration.prevExperience(), response.prevExperience());

    }

    @Test
    void testCreateRegistrationWithInvalidStudent() {

        String courseId = createCourse(createPerson("Teacher"));
        RegistrationCreateDto registration = new RegistrationCreateDto("invalidId",
                courseId, "Pending", "10",
                "about", true, true);

        Error response = given()
                .body(registration)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(URL)
                .then()
                .statusCode(400)
                .extract().as(Error.class);

        assertEquals("Person not found", response.getMessage());
        assertEquals(400, response.getStatus());
    }

    @Test
    void testCreateRegistrationWithInvalidCourse() {

        String studentId = createPerson("Student");
        RegistrationCreateDto registration = new RegistrationCreateDto(studentId,
                "invalidId", "Pending", "10",
                "about", true, true);

        Error response = given()
                .body(registration)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(URL)
                .then()
                .statusCode(400)
                .extract().as(Error.class);

        assertEquals("course not found", response.getMessage());
        assertEquals(400, response.getStatus());
    }

    @Test
    void testGetRegistrationById() {

    }

    @Test
    void testGetRegistrationByInvalidId() {

    }
}