package com.mindera.finalproject.be.Project;

import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Project;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ProjectControllerTests {

    private static List<String> studentIds;
    private static String teacherId;

    private static String courseId;
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

    private DynamoDbTable<Project> projectTable;
    private DynamoDbTable<Person> personTable;
    private DynamoDbTable<Course> courseTable;

    @BeforeEach
    public void setUp() {
        projectTable = dynamoEnhancedClient.table("Project", TableSchema.fromBean(Project.class));
        personTable = dynamoEnhancedClient.table("Person", TableSchema.fromBean(Person.class));
        courseTable = dynamoEnhancedClient.table("Course", TableSchema.fromBean(Course.class));
        try {
            personTable.createTable();
            courseTable.createTable();
            projectTable.createTable();
            Thread.sleep(100);
        } catch (Exception e) {
            projectTable.deleteTable();
            courseTable.deleteTable();
            personTable.deleteTable();
            setUp();
        }
        List<PersonCreateDto> students = Arrays.asList(
                new PersonCreateDto("example@email.com", "John", "Doe",
                        "Student", "Test", LocalDate.of(1990, 1, 1),
                        "Porto", "cv"),
                new PersonCreateDto("example@email.com", "Jane", "Doe",
                        "Student", "Test", LocalDate.of(1991, 1, 1),
                        "Porto", "cv")
        );
        studentIds = students.stream()
                .map(student -> given()
                        .body(student)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .when().post("/api/v1/persons")
                        .then()
                        .statusCode(201)
                        .extract().jsonPath().getString("id"))
                .collect(Collectors.toList());


        PersonCreateDto teacher = new PersonCreateDto("example@email.com", "John", "Doe",
                "Teacher", "Test", LocalDate.of(1990, 1, 1), "Porto",
                "123456789");

        teacherId = given()
                .body(teacher)
                .header(org.apache.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post("/api/v1/persons")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");


        CourseCreateDto exampleCourse = new CourseCreateDto(courseName, courseEdition, teacherId, courseSyllabus,
                courseProgram, courseSchedule, coursePrice, courseDuration, courseLocation);

        courseId = given()
                .body(exampleCourse)
                .header(org.apache.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post("/api/v1/courses")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    @AfterEach
    public void tearDown() {
        personTable.deleteTable();
        courseTable.deleteTable();
        projectTable.deleteTable();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public String createProject() {

        ProjectCreateDto newProject = new ProjectCreateDto("Project name", studentIds,
                courseId, "https://github.com/user/repo");
        return given()
                .body(newProject)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().post("/api/v1/projects")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");
    }

    @Test
    public void testGetAllProjects() {
        given()
                .when().get("/api/v1/projects")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testGetById() {

        String projectId = createProject();

        ProjectPublicDto response = given()
                .when().get("/api/v1/projects/" + projectId)
                .then()
                .statusCode(200)
                .extract().as(ProjectPublicDto.class);

        assertEquals(projectId, response.id());
        assertEquals("Project name", response.name());
        assertEquals(studentIds, response.students().stream().map(studentIds -> studentIds.id()).collect(Collectors.toList()));
        assertEquals(courseId, response.course().id());
        assertEquals("https://github.com/user/repo", response.gitHubRepo());

    }

    @Test
    public void testGetByIdNotFound() {

        String projectId = "XXXXXXX"; //non-existing ID

        given()
                .pathParam("id", projectId)
                .when().get("/api/v1/projects/{id}")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testCreate() {
        ProjectCreateDto newProject = new ProjectCreateDto("Project name", studentIds,
                courseId, "https://github.com/user/repo");
        given()
                .contentType(ContentType.JSON)
                .body(newProject)
                .when().post("/api/v1/projects")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());
    }

    @Test
    public void testUpdate() {
        String projectId = createProject();

        ProjectCreateDto projectCreateDto = new ProjectCreateDto("Project name", studentIds,
                courseId, "https://github.com/user/repo");

        given()
                .pathParam("id", projectId)
                .contentType(ContentType.JSON)
                .body(projectCreateDto)
                .when().put("/api/v1/projects/{id}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testUpdateNotFound() {

        String projectId = "XXXXXXX"; //non-existing ID
        ProjectCreateDto projectCreateDto = new ProjectCreateDto("Project name", studentIds,
                courseId, "https://github.com/user/repo");
        given()
                .pathParam("id", projectId)
                .contentType(ContentType.JSON)
                .body(projectCreateDto)
                .when().put("/api/v1/projects/{id}")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testDelete() {

        String projectId = createProject();
        given()
                .pathParam("id", projectId)
                .when().delete("/api/v1/projects/{id}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    void testDeleteNotFound() {

        String projectId = "XXXXXXX"; //non-existing ID
        given()
                .pathParam("id", projectId)
                .when().delete("/api/v1/projects/{id}")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

}
