package com.mindera.finalproject.be.Registration;

import com.mindera.finalproject.be.aspect.Error;
import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
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

        private static String studentId;
        private static String courseId;
        private static String teacherId;
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

                PersonCreateDto student = new PersonCreateDto("example@email.com", "John", "Doe",
                                "Student", "Test1", LocalDate.of(1990, 1, 1), "Porto", "123456789");

                PersonCreateDto teacher = new PersonCreateDto("exampleTeacher@email.com", "Jane", "Doe",
                                "Teacher", "Test2", LocalDate.of(1990, 1, 1), "Porto", "123456789");

                studentId = given()
                                .body(student)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .when().post("/api/v1/persons")
                                .then()
                                .statusCode(201)
                                .extract().jsonPath().getString("id");

                teacherId = given()
                                .body(teacher)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .when().post("/api/v1/persons")
                                .then()
                                .statusCode(201)
                                .extract().jsonPath().getString("id");

                CourseCreateDto course = new CourseCreateDto("Test Course", 1, teacherId, "Syllabus", "Program",
                                "Schedule",
                                new BigDecimal(100), 10, "Porto");

                courseId = given()
                                .body(course)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .when().post("/api/v1/courses")
                                .then()
                                .statusCode(201)
                                .extract().jsonPath().getString("id");
        }

        @AfterEach
        void tearDown() {
                courseTable.deleteTable();
                personTable.deleteTable();
                registrationTable.deleteTable();
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
        void testGetAllRegistrationsWith5Registrations(){
                RegistrationCreateDto registration1 = new RegistrationCreateDto(studentId, courseId, "Pending", "10",
                                "about", true, true);
                RegistrationCreateDto registration2 = new RegistrationCreateDto(studentId, courseId, "Pending", "10",
                                "about", true, true);
                RegistrationCreateDto registration3 = new RegistrationCreateDto(studentId, courseId, "Pending", "10",
                                "about", true, true);
                RegistrationCreateDto registration4 = new RegistrationCreateDto(studentId, courseId, "Pending", "10",
                                "about", true, true);
                RegistrationCreateDto registration5 = new RegistrationCreateDto(studentId, courseId, "Pending", "10",
                                "about", true, true);

                given()
                                .body(registration1)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .when().post(URL)
                                .then()
                                .statusCode(201);

                given()
                                .body(registration2)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .when().post(URL)
                                .then()
                                .statusCode(201);

                given()
                                .body(registration3)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .when().post(URL)
                                .then()
                                .statusCode(201);

                given()
                                .body(registration4)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .when().post(URL)
                                .then()
                                .statusCode(201);

                given()
                                .body(registration5)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .when().post(URL)
                                .then()
                                .statusCode(201);

                given()
                                .when().get(URL)
                                .then()
                                .body("size()", equalTo(5));
        }

        @Test
        void testCreateRegistration() {
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
                RegistrationCreateDto registration = new RegistrationCreateDto("invalidId", courseId, "Pending", "10",
                                "about", true, true);

                given()
                                .body(registration)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                                .when().post(URL)
                                .then()
                                .statusCode(400)
                                .body("message", equalTo(Error.INVALID_PERSON_ID));
        }

        @Test
        void testCreateRegistrationWithInvalidCourse() {
        }

        @Test
        void testCreateRegistrationWithInvalidStatus() {
        }

        @Test
        void testCreateRegistrationWithInvalidFinalGrade() {
        }

        @Test
        void testCreateRegistrationWithInvalidAboutYou() {
        }

        @Test
        void testCreateRegistrationWithInvalidPrevKnowledge() {
        }

        @Test
        void testCreateRegistrationWithInvalidPrevExperience() {
        }

        @Test
        void testGetRegistrationById(){

        }

        @Test
        void testGetRegistrationByInvalidId(){

        }
}