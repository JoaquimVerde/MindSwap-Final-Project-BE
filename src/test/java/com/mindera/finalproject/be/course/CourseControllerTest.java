package com.mindera.finalproject.be.course;

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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class CourseControllerTest {

    private static String teacherId;
    private final String API_PATH = "/api/v1/courses";
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
        }

        PersonCreateDto teacher = new PersonCreateDto("example@email.com", "John", "Doe",
                "Teacher", "Teste", LocalDate.of(1990, 1, 1), "Porto", "123456789");

        teacherId = given()
                .body(teacher)
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

    public String createCourse() {
        CourseCreateDto exampleCourse = new CourseCreateDto("Frontend", 1, teacherId, "HTML, CSS, JavaScript", "Frontend", "Monday 10-18", new BigDecimal("900.13"), 30, "Porto");

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
        CourseCreateDto exampleCourse = new CourseCreateDto("Frontend", 1, teacherId, "HTML, CSS, JavaScript", "Frontend", "Monday 10-18", new BigDecimal("900.00"), 30, "Porto");

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
        assertEquals(exampleCourse.location(), response.location());
        assertEquals(teacherId, response.teacher().id());
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
            createCourse();
        }

        given()
                .when().get(API_PATH)
                .then()
                .statusCode(200)
                .and()
                .body("size()", equalTo(amount));
    }

    @Test
    void testGetCourseById() {
        String courseId = createCourse();

        CoursePublicDto response = given()
                .when().get(API_PATH + "/" + courseId)
                .then()
                .statusCode(200)
                .extract().as(CoursePublicDto.class);

        assertEquals(courseId, response.id());
        assertEquals("Frontend", response.name());
        assertEquals(1, response.edition());
        assertEquals("HTML, CSS, JavaScript", response.syllabus());
        assertEquals("Frontend", response.program());
        assertEquals("Monday 10-18", response.schedule());
        assertEquals(new BigDecimal("900.13"), response.price());
        assertEquals(30, response.duration());
        assertEquals("Porto", response.location());
        assertEquals(teacherId, response.teacher().id());
    }

    @Test
    void testGetByIdWithNonExistentCourse() {
        given()
                .when().get(API_PATH + "/123")
                .then()
                .statusCode(404);
    }
}
