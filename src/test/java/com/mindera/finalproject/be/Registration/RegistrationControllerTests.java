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
import java.util.List;

import static com.mindera.finalproject.be.messages.Messages.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RegistrationControllerTests {

    private final String URL = "/api/v1/registration";
    private final String status = "Applied";
    private final Integer finalGrade = 10;
    private final String aboutYou = "about";
    private final boolean prevKnowledge = true;
    private final boolean prevExperience = true;

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
        RegistrationCreateDto registration = new RegistrationCreateDto(studentId, courseId, status, finalGrade,
                aboutYou, prevKnowledge, prevExperience);

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
        String courseId = createCourse(createPerson("Teacher"));
        int amount = 5;
        for (int i = 0; i < amount; i++) {
            String studentId = createPerson("Student");
            createRegistration(studentId, courseId);
        }

        given()
                .when().get(URL)
                .then()
                .body("size()", equalTo(5));
    }

    @Test
    void testGetAllRegistrationsWith5Registrations2Deleted() {
        String courseId = createCourse(createPerson("Teacher"));
        int amount = 5;
        for (int i = 0; i < amount; i++) {
            String studentId = createPerson("Student");
           String id = createRegistration(studentId, courseId);
           if(i % 2 == 1) {
               given()
                       .when().delete(URL + "/delete/" + id)
                       .then()
                       .statusCode(200);
           }
        }

        given()
                .when().get(URL)
                .then()
                .body("size()", equalTo(3));
    }

    @Test
    void testGetAllRegistrationsPaged() {
        String courseId = createCourse(createPerson("Teacher"));
        int amount = 9;
        for (int i = 0; i < amount; i++) {
            String studentId = createPerson("Student");
            createRegistration(studentId, courseId);
        }

        given()
                .queryParam("page", 0)
                .queryParam("limit", 5)
                .when().get(URL)
                .then()
                .body("size()", equalTo(5));

        given()
                .queryParam("page", 1)
                .queryParam("limit", 5)
                .when().get(URL)
                .then()
                .body("size()", equalTo(4));
    }

    @Test
    void testCreateRegistration() {
        String studentId = createPerson("Student");
        String courseId = createCourse(createPerson("Teacher"));

        RegistrationCreateDto registration = new RegistrationCreateDto(studentId, courseId, status, finalGrade,
                aboutYou, prevKnowledge, prevExperience);

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
        assertEquals(registration.status().toUpperCase(), response.status());
        assertEquals(registration.finalGrade(), response.finalGrade());
        assertEquals(registration.aboutYou(), response.aboutYou());
        assertEquals(registration.prevKnowledge(), response.prevKnowledge());
        assertEquals(registration.prevExperience(), response.prevExperience());

    }

    @Test
    void testCreateRegistrationWithInvalidStudent() {

        String courseId = createCourse(createPerson("Teacher"));
        RegistrationCreateDto registration = new RegistrationCreateDto("invalidId",
                courseId, status, finalGrade,
                aboutYou, prevKnowledge, prevExperience);

        Error response = given()
                .body(registration)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(URL)
                .then()
                .statusCode(404)
                .extract().as(Error.class);

        assertEquals(PERSON_NOT_FOUND + "invalidId", response.getMessage());
        assertEquals(404, response.getStatus());
    }

    @Test
    void testCreateRegistrationWithInvalidCourse() {

        String studentId = createPerson("Student");
        RegistrationCreateDto registration = new RegistrationCreateDto(studentId,
                "invalidId", status, finalGrade,
                aboutYou, prevKnowledge, prevExperience);

        Error response = given()
                .body(registration)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(URL)
                .then()
                .statusCode(404)
                .extract().as(Error.class);

        assertEquals(COURSE_NOT_FOUND + "invalidId", response.getMessage());
        assertEquals(404, response.getStatus());
    }

    @Test
    void testCreateRegistrationWithEmptyData() {
        RegistrationCreateDto registration = new RegistrationCreateDto("", "", "", null, "", null, null);

        Error response = given()
                .body(registration)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(URL)
                .then()
                .statusCode(400)
                .extract().as(Error.class);

        assertEquals(400, response.getStatus());
        assertTrue(response.getMessage().contains(NON_EMPTY_PERSONID));
        assertTrue(response.getMessage().contains(NON_EMPTY_COURSEID));
        assertTrue(response.getMessage().contains(NON_EMPTY_ABOUT_YOU));
        assertTrue(response.getMessage().contains(NON_EMPTY_PREVKNOWLEDGE));
        assertTrue(response.getMessage().contains(NON_EMPTY_PREVEXPERIENCE));
        assertTrue(response.getMessage().contains(INVALID_REGISTRATION_STATUS));
    }

    @Test
    void testCreateRegistrationWithGradeOutOfRange() {
        String studentId = createPerson("Student");
        String courseId = createCourse(createPerson("Teacher"));
        RegistrationCreateDto registration = new RegistrationCreateDto(studentId, courseId, status, 21, aboutYou, prevKnowledge, prevExperience);
        RegistrationCreateDto registration2 = new RegistrationCreateDto(studentId, courseId, status, -1, aboutYou, prevKnowledge, prevExperience);

        Error response = given()
                .body(registration)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(URL)
                .then()
                .statusCode(400)
                .extract().as(Error.class);

        Error response2 = given()
                .body(registration)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(URL)
                .then()
                .statusCode(400)
                .extract().as(Error.class);

        assertEquals(400, response.getStatus());
        assertTrue(response.getMessage().contains(""));
        assertEquals(400, response2.getStatus());
        assertTrue(response2.getMessage().contains(""));
    }

    @Test
    void testCreateRegistrationWithDuplicateRegistration() {
        String studentId = createPerson("Student");
        String courseId = createCourse(createPerson("Teacher"));
        RegistrationCreateDto registration = new RegistrationCreateDto(studentId, courseId, status, finalGrade,
                aboutYou, prevKnowledge, prevExperience);

        given()
                .body(registration)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(URL)
                .then()
                .statusCode(201);

        Error response = given()
                .body(registration)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(URL)
                .then()
                .statusCode(409)
                .extract().as(Error.class);

        assertEquals(409, response.getStatus());
        assertEquals(REGISTRATION_ALREADY_EXISTS, response.getMessage());
    }

    @Test
    void testGetRegistrationById() {
        String studentId = createPerson("Student");
        String courseId = createCourse(createPerson("Teacher"));
        String registrationId = createRegistration(studentId, courseId);

        RegistrationPublicDto response = given()
                .when().get(URL + "/" + registrationId)
                .then()
                .statusCode(200)
                .extract().as(RegistrationPublicDto.class);

        assertTrue(response.id().startsWith("REGISTRATION#"));
        assertEquals(studentId, response.student().id());
        assertEquals(courseId, response.course().id());
        assertEquals(status.toUpperCase(), response.status());
        assertEquals(finalGrade, response.finalGrade());
        assertEquals(aboutYou, response.aboutYou());
        assertEquals(prevKnowledge, response.prevKnowledge());
        assertEquals(prevExperience, response.prevExperience());

    }

    @Test
    void testGetRegistrationByInvalidId() {
        Error response = given()
                .when().get(URL + "/invalidId")
                .then()
                .statusCode(404)
                .extract().as(Error.class);

        assertEquals(REGISTRATION_NOT_FOUND + "invalidId", response.getMessage());
        assertEquals(404, response.getStatus());
    }

    @Test
    void testUpdateRegistration() {
        String studentId = createPerson("Student");
        String courseId = createCourse(createPerson("Teacher"));
        String registrationId = createRegistration(studentId, courseId);

        RegistrationCreateDto registration = new RegistrationCreateDto(studentId, courseId, "Accepted", 20,
                "newAboutYou", false, false);

        RegistrationPublicDto response = given()
                .body(registration)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().put(URL + "/" + registrationId)
                .then()
                .statusCode(200)
                .extract().as(RegistrationPublicDto.class);

        assertTrue(response.id().startsWith("REGISTRATION#"));
        assertEquals(studentId, response.student().id());
        assertEquals(courseId, response.course().id());
        assertEquals("ACCEPTED", response.status());
        assertEquals(20, response.finalGrade());
        assertEquals("newAboutYou", response.aboutYou());
        assertFalse(response.prevKnowledge());
        assertFalse(response.prevExperience());
    }

    @Test
    void testUpdateRegistrationWithInvalidId() {
        String studentId = createPerson("Student");
        String courseId = createCourse(createPerson("Teacher"));
        createRegistration(studentId, courseId);

        RegistrationCreateDto registration = new RegistrationCreateDto(studentId, courseId, "Accepted", 20,
                "newAboutYou", false, false);

        Error response = given()
                .body(registration)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().put(URL + "/invalidId")
                .then()
                .statusCode(404)
                .extract().as(Error.class);

        assertEquals(REGISTRATION_NOT_FOUND + "invalidId", response.getMessage());
        assertEquals(404, response.getStatus());
    }

// TODO ver COMO QUEREMOS FAZER UPDATE

//    @Test
//    void testUpdateRegistrationWithInvalidStudent() {
//        String studentId = createPerson("Student");
//        String courseId = createCourse(createPerson("Teacher"));
//        String registrationId = createRegistration(studentId, courseId);
//
//        RegistrationCreateDto registration = new RegistrationCreateDto("invalidId", courseId, "ENROLLED", 20,
//                "newAboutYou", false, false);
//
//        Error response = given()
//                .body(registration)
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
//                .when().put(URL + "/" + registrationId)
//                .then()
//                .statusCode(404)
//                .extract().as(Error.class);
//
//        assertEquals(PERSON_NOT_FOUND + "invalidId", response.getMessage());
//        assertEquals(404, response.getStatus());
//    }

    @Test
    void testDeleteRegistration() {
        String studentId = createPerson("Student");
        String courseId = createCourse(createPerson("Teacher"));
        String registrationId = createRegistration(studentId, courseId);

        given()
                .when().delete(URL + "/delete/" + registrationId)
                .then()
                .statusCode(200);

        given()
                .when().get(URL)
                .then()
                .body("size()", equalTo(0));
    }

    @Test
    void testDeleteRegistrationWithInvalidId() {
        given()
                .when().delete(URL + "/delete/invalidId")
                .then()
                .statusCode(404);
    }

    @Test
    void testGetRegistrationByPersonId() {
        String studentId = createPerson("Student");
        String courseId = createCourse(createPerson("Teacher"));
        String registrationId = createRegistration(studentId, courseId);

        List<RegistrationPublicDto> response = given()
                .when().get(URL + "/student/" + studentId)
                .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", RegistrationPublicDto.class);

        assertEquals(registrationId, response.get(0).id());

    }
}