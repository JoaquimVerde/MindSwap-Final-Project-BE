package com.mindera.finalproject.be.course;

import com.mindera.finalproject.be.aspect.Error;
import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
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
class CourseControllerTest {

    private static String teacherId;
    private static String studentId;
    private final String API_PATH = "/api/v1/courses";
    private final String courseName = "Frontend";
    private final int courseEdition = 1;
    private final String courseSyllabus = "HTML, CSS, JavaScript";
    private final String courseProgram = "Frontend";
    private final String courseSchedule = "Monday 10-18";
    private final BigDecimal coursePrice = new BigDecimal("900.13");
    private final int courseDuration = 30;
    private final String courseLocation = "Porto";
    @Inject
    private DynamoDbEnhancedClient dynamoEnhancedClient;
    private DynamoDbTable<Course> courseTable;
    private DynamoDbTable<Person> personTable;

    @BeforeEach
    void setUp() {
        courseTable = dynamoEnhancedClient.table("Course", TableSchema.fromBean(Course.class));
        personTable = dynamoEnhancedClient.table("Person", TableSchema.fromBean(Person.class));
        try {
            courseTable.createTable();
            personTable.createTable();
            Thread.sleep(100);
        } catch (Exception e) {
            courseTable.deleteTable();
            personTable.deleteTable();
            setUp();
            return;
        }

        PersonCreateDto teacher = new PersonCreateDto("TEACHER#123123", "example@email.com", "John", "Doe",
                "Teacher", "Teste", LocalDate.of(1990, 1, 1), "Porto", "123456789");

        PersonCreateDto student = new PersonCreateDto("STUDENT#123123", "example@email.com", "John", "Doe",
                "Student", "Teste", LocalDate.of(1990, 1, 1), "Porto", "123456789");

        teacherId = given()
                .body(teacher)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post("/api/v1/persons")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

        studentId = given()
                .body(student)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post("/api/v1/persons")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    @AfterEach
    void tearDown() {
        courseTable.deleteTable();
        personTable.deleteTable();
    }

    public String createCourse(String location, int edition, String teacherId) {
        CourseCreateDto exampleCourse = new CourseCreateDto(courseName, edition, teacherId, courseSyllabus, courseProgram, courseSchedule, coursePrice, courseDuration, location);

        return given()
                .body(exampleCourse)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(API_PATH)
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    @Test
    void testCreateCourse() {
        CourseCreateDto exampleCourse = new CourseCreateDto(courseName, courseEdition, teacherId, courseSyllabus, courseProgram, courseSchedule, coursePrice, courseDuration, courseLocation);

        CoursePublicDto response = given()
                .body(exampleCourse)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(API_PATH)
                .then()
                .statusCode(201)
                .extract().as(CoursePublicDto.class);

        assertNotNull(response.id());
        assertEquals(exampleCourse.name(), response.name());
        assertEquals(exampleCourse.edition(), response.edition());
        assertEquals(exampleCourse.syllabus(), response.syllabus());
        assertEquals(exampleCourse.program(), response.program());
        assertEquals(exampleCourse.schedule(), response.schedule());
        assertEquals(exampleCourse.price(), response.price());
        assertEquals(exampleCourse.duration(), response.duration());
        assertEquals(exampleCourse.location().toUpperCase(), response.location());
        assertEquals(teacherId, response.teacher().id());
    }

    @Test
    void testCreateCourseWithInvalidData() {
        CourseCreateDto exampleCourse = new CourseCreateDto("1231232#%", -1, "asdasd", "", "", "", new BigDecimal("-100"), -1, "");

        Error response = given()
                .body(exampleCourse)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(API_PATH)
                .then()
                .statusCode(400)
                .extract().as(Error.class);

        assertEquals(400, response.getStatus());
        assertTrue(response.getMessage().contains(INVALID_NAME));
        assertTrue(response.getMessage().contains(INVALID_EDITION));
        assertTrue(response.getMessage().contains(NON_EMPTY_SYLLABUS));
        assertTrue(response.getMessage().contains(NON_EMPTY_PROGRAM));
        assertTrue(response.getMessage().contains(NON_EMPTY_SCHEDULE));
        assertTrue(response.getMessage().contains(NON_NEGATIVE_PRICE));
        assertTrue(response.getMessage().contains(NON_NEGATIVE_DURATION));
        assertTrue(response.getMessage().contains(NON_EMPTY_LOCATION));
    }

    @Test
    void testCreateCourseWithEmptyData() {
        CourseCreateDto exampleCourse = new CourseCreateDto("", 0, "", "", "", "", null, null, "");

        Error response = given()
                .body(exampleCourse)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(API_PATH)
                .then()
                .statusCode(400)
                .extract().as(Error.class);

        assertEquals(400, response.getStatus());
        assertTrue(response.getMessage().contains(NON_EMPTY_NAME));
        assertTrue(response.getMessage().contains(INVALID_EDITION));
        assertTrue(response.getMessage().contains(NON_EMPTY_SYLLABUS));
        assertTrue(response.getMessage().contains(NON_EMPTY_PROGRAM));
        assertTrue(response.getMessage().contains(NON_EMPTY_SCHEDULE));
        assertTrue(response.getMessage().contains(NON_EMPTY_PRICE));
        assertTrue(response.getMessage().contains(NON_EMPTY_DURATION));
        assertTrue(response.getMessage().contains(NON_EMPTY_LOCATION));
    }

    @Test
    void testCreateCourseWithDuplicateCourse() {
        createCourse(courseLocation, courseEdition, teacherId);

        CourseCreateDto exampleCourse = new CourseCreateDto(courseName, courseEdition, teacherId, courseSyllabus, courseProgram, courseSchedule, coursePrice, courseDuration, courseLocation);

        Error response = given()
                .body(exampleCourse)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(API_PATH)
                .then()
                .statusCode(409)
                .extract().as(Error.class);

        assertEquals(409, response.getStatus());
        assertEquals(COURSE_ALREADY_EXISTS, response.getMessage());
    }

    @Test
    void testCreateCourseWithInvalidTeacher() {
        CourseCreateDto exampleCourse = new CourseCreateDto(courseName, courseEdition, "PERSON#1", courseSyllabus, courseProgram, courseSchedule, coursePrice, courseDuration, courseLocation);

        Error response = given()
                .body(exampleCourse)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(API_PATH)
                .then()
                .statusCode(404)
                .extract().as(Error.class);

        assertEquals(404, response.getStatus());
        assertEquals(PERSON_NOT_FOUND + "PERSON#1", response.getMessage());
    }

    @Test
    void testCreateCourseWithStudentId() {
        CourseCreateDto exampleCourse = new CourseCreateDto(courseName, courseEdition, studentId, courseSyllabus, courseProgram, courseSchedule, coursePrice, courseDuration, courseLocation);

        Error response = given()
                .body(exampleCourse)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(API_PATH)
                .then()
                .statusCode(400)
                .extract().as(Error.class);

        assertEquals(400, response.getStatus());
        assertEquals(PERSON_NOT_A_TEACHER, response.getMessage());
    }

    @Test
    void testCreateCourseWithNullTeacher() {
        CourseCreateDto exampleCourse = new CourseCreateDto(courseName, courseEdition, null, courseSyllabus, courseProgram, courseSchedule, coursePrice, courseDuration, courseLocation);

        CoursePublicDto response = given()
                .body(exampleCourse)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post(API_PATH)
                .then()
                .statusCode(201)
                .extract().as(CoursePublicDto.class);

        assertNull(response.teacher());

        given()
                .when().get(API_PATH + "/" + response.id())
                .then()
                .statusCode(200)
                .and()
                .body("teacher", equalTo(null));
    }

    @Test
    void testGetAllCoursesWithNoCourses() {
        given()
                .when().get(API_PATH)
                .then()
                .statusCode(200)
                .and()
                .body("size()", equalTo(0));
    }

    @Test
    void testGetAllCoursesWith5Courses() {
        int amount = 5;
        for (int i = 0; i < amount; i++) {
            createCourse(courseLocation, i + 1, teacherId);
        }

        given()
                .when().get(API_PATH)
                .then()
                .statusCode(200)
                .and()
                .body("size()", equalTo(amount));
    }

    @Test
    void testGetAllCoursesWith5and2Deleted() {
        int amount = 5;
        for (int i = 0; i < amount; i++) {
            String courseId = createCourse(courseLocation, i + 1, teacherId);
            if (i % 2 == 1) {
                given()
                        .when().delete(API_PATH + "/" + courseId)
                        .then()
                        .statusCode(200);
            }
        }

        given()
                .when().get(API_PATH)
                .then()
                .statusCode(200)
                .and()
                .body("size()", equalTo(3));
    }

    @Test
    void testGetAllCoursesWithNullTeachers() {
        int amount = 5;
        for (int i = 0; i < amount; i++) {
            createCourse(courseLocation, i + 1, null);
        }

        List<CoursePublicDto> response = given()
                .when().get(API_PATH)
                .then()
                .statusCode(200)
                .extract().jsonPath().getList(".", CoursePublicDto.class);

        for (CoursePublicDto course : response) {
            assertNotNull(course.id());
            assertNull(course.teacher());
        }
    }

    @Test
    void testGetAllPagedCourses() {
        int amount = 9;
        for (int i = 0; i < amount; i++) {
            createCourse(courseLocation, i + 1, teacherId);
        }

        given()
                .when().get(API_PATH + "?page=0&limit=5")
                .then()
                .statusCode(200)
                .and()
                .body("size()", equalTo(5));
        given()
                .when().get(API_PATH + "?page=1&limit=5")
                .then()
                .statusCode(200)
                .and()
                .body("size()", equalTo(4));

    }


    @Test
    void testGetCourseById() {
        String courseId = createCourse(courseLocation, courseEdition, teacherId);

        CoursePublicDto response = given()
                .when().get(API_PATH + "/" + courseId)
                .then()
                .statusCode(200)
                .extract().as(CoursePublicDto.class);

        assertEquals(courseId, response.id());
        assertEquals(courseName, response.name());
        assertEquals(courseEdition, response.edition());
        assertEquals(courseSyllabus, response.syllabus());
        assertEquals(courseProgram, response.program());
        assertEquals(courseSchedule, response.schedule());
        assertEquals(coursePrice, response.price());
        assertEquals(courseDuration, response.duration());
        assertEquals(courseLocation.toUpperCase(), response.location());
        assertEquals(teacherId, response.teacher().id());
    }

    @Test
    void testGetByIdWithNonExistentCourse() {
        given()
                .when().get(API_PATH + "/123")
                .then()
                .statusCode(404);
    }

    @Test
    void testGetByIdWithDeletedCourse() {
        String courseId = createCourse(courseLocation, courseEdition, teacherId);

        given()
                .when().delete(API_PATH + "/" + courseId)
                .then()
                .statusCode(200);

        given()
                .when().get(API_PATH + "/" + courseId)
                .then()
                .statusCode(200);
    }

    @Test
    void testGetByIdWithNullTeacher() {
        String courseId = createCourse(courseLocation, courseEdition, null);

        CoursePublicDto response = given()
                .when().get(API_PATH + "/" + courseId)
                .then()
                .statusCode(200)
                .extract().as(CoursePublicDto.class);

        assertNotNull(response.id());
        assertNull(response.teacher());
    }

    @Test
    void testGetByIdWithDeletedTeacher() {
        String courseId = createCourse(courseLocation, courseEdition, teacherId);

        given()
                .when().delete("/api/v1/persons/" + teacherId)
                .then()
                .statusCode(200);

        CoursePublicDto response = given()
                .when().get(API_PATH + "/" + courseId)
                .then()
                .statusCode(200)
                .extract().as(CoursePublicDto.class);

        assertNotNull(response.id());
        assertNull(response.teacher());
    }

    @Test
    void testGetByLocation() {
        int amount = 3;
        for (int i = 0; i < amount; i++) {
            createCourse(courseLocation, i + 1, teacherId);
        }
        for (int i = 0; i < 2; i++) {
            createCourse("NonExistentLocation", i + amount + 1, teacherId);
        }

        given()
                .when().get(API_PATH + "/location/" + courseLocation)
                .then()
                .statusCode(200)
                .and()
                .body("size()", equalTo(amount));
    }

    @Test
    void testGetByLocationWithNonExistentLocation() {
        createCourse(courseLocation, courseEdition, teacherId);

        given()
                .when().get(API_PATH + "/location/NonExistentLocation")
                .then()
                .statusCode(200)
                .and()
                .body("size()", equalTo(0));
    }

    @Test
    void testGetByLocationWithDeletedCourses() {
        int amount = 3;
        for (int i = 0; i < amount; i++) {
            String courseId = createCourse(courseLocation, i + 1, teacherId);
            if (i % 2 == 1) {
                given()
                        .when().delete(API_PATH + "/" + courseId)
                        .then()
                        .statusCode(200);
            }
        }
        createCourse("NonExistentLocation", amount + 1, teacherId);

        given()
                .when().get(API_PATH + "/location/" + courseLocation)
                .then()
                .statusCode(200)
                .and()
                .body("size()", equalTo(2));
    }

    @Test
    void testGetByLocationPaged() {
        int amount = 9;
        for (int i = 0; i < amount; i++) {
            createCourse(courseLocation, i + 1, teacherId);
        }

        given()
                .when().get(API_PATH + "/location/" + courseLocation + "?page=0&limit=5")
                .then()
                .statusCode(200)
                .and()
                .body("size()", equalTo(5));
        given()
                .when().get(API_PATH + "/location/" + courseLocation + "?page=1&limit=5")
                .then()
                .statusCode(200)
                .and()
                .body("size()", equalTo(4));
    }

    @Test
    void testUpdateCourse() {
        String courseId = createCourse(courseLocation, courseEdition, teacherId);

        CourseCreateDto updatedCourse = new CourseCreateDto("Backend", 2, teacherId, "Java, Spring", "Backend", "Tuesday 10-18", new BigDecimal("1000.13"), 60, "Lisbon");

        CoursePublicDto response = given()
                .body(updatedCourse)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().put(API_PATH + "/" + courseId)
                .then()
                .statusCode(200)
                .extract().as(CoursePublicDto.class);

        assertEquals(courseId, response.id());
        assertEquals(updatedCourse.name(), response.name());
        assertEquals(updatedCourse.edition(), response.edition());
        assertEquals(updatedCourse.syllabus(), response.syllabus());
        assertEquals(updatedCourse.program(), response.program());
        assertEquals(updatedCourse.schedule(), response.schedule());
        assertEquals(updatedCourse.price(), response.price());
        assertEquals(updatedCourse.duration(), response.duration());
        assertEquals(updatedCourse.location(), response.location());
        assertEquals(teacherId, response.teacher().id());
    }

    @Test
    void testUpdateCourseWithNonExistentCourse() {
        CourseCreateDto updatedCourse = new CourseCreateDto("Backend", 2, teacherId, "Java, Spring", "Backend", "Tuesday 10-18", new BigDecimal("1000.13"), 60, "Lisbon");

        given()
                .body(updatedCourse)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().put(API_PATH + "/123")
                .then()
                .statusCode(404);
    }

    @Test
    void testUpdateCourseWithEmptyData() {
        String courseId = createCourse(courseLocation, courseEdition, teacherId);

        CourseCreateDto updatedCourse = new CourseCreateDto("", null, "", "", "", "", null, null, "");

        Error response = given()
                .body(updatedCourse)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().put(API_PATH + "/" + courseId)
                .then()
                .statusCode(400)
                .extract().as(Error.class);

        assertEquals(400, response.getStatus());
        assertTrue(response.getMessage().contains(NON_EMPTY_NAME));
        assertTrue(response.getMessage().contains(NON_EMPTY_EDITION));
        assertTrue(response.getMessage().contains(NON_EMPTY_SYLLABUS));
        assertTrue(response.getMessage().contains(NON_EMPTY_PROGRAM));
        assertTrue(response.getMessage().contains(NON_EMPTY_SCHEDULE));
        assertTrue(response.getMessage().contains(NON_EMPTY_PRICE));
        assertTrue(response.getMessage().contains(NON_EMPTY_DURATION));
        assertTrue(response.getMessage().contains(NON_EMPTY_LOCATION));

    }

    @Test
    void testUpdateWithInvalidTeacher() {
        String courseId = createCourse(courseLocation, courseEdition, teacherId);

        CourseCreateDto updatedCourse = new CourseCreateDto("Backend", 2, "PERSON#1", "Java, Spring", "Backend", "Tuesday 10-18", new BigDecimal("1000.13"), 60, "Lisbon");

        Error response = given()
                .body(updatedCourse)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().put(API_PATH + "/" + courseId)
                .then()
                .statusCode(404)
                .extract().as(Error.class);

        assertEquals(404, response.getStatus());
        assertEquals(PERSON_NOT_FOUND + "PERSON#1", response.getMessage());
    }

    @Test
    void testUpdateCourseWithStudentId() {
        String courseId = createCourse(courseLocation, courseEdition, teacherId);
        CourseCreateDto updatedCourse = new CourseCreateDto("Backend", 2, studentId, "Java, Spring", "Backend", "Tuesday 10-18", new BigDecimal("1000.13"), 60, "Lisbon");

        Error response = given()
                .body(updatedCourse)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().put(API_PATH + "/" + courseId)
                .then()
                .statusCode(400)
                .extract().as(Error.class);

        assertEquals(400, response.getStatus());
        assertEquals(PERSON_NOT_A_TEACHER, response.getMessage());
    }

    @Test
    void testDeleteCourse() {
        String courseId = createCourse(courseLocation, courseEdition, teacherId);

        given()
                .when().delete(API_PATH + "/" + courseId)
                .then()
                .statusCode(200);

        given()
                .when().get(API_PATH)
                .then()
                .statusCode(200)
                .and()
                .body("size()", equalTo(0));
    }

    @Test
    void testDeleteWithNonExistentCourse() {
        given()
                .when().delete(API_PATH + "/123")
                .then()
                .statusCode(404);
    }
}
