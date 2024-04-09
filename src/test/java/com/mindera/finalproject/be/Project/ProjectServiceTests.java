package com.mindera.finalproject.be.Project;

import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;
import com.mindera.finalproject.be.entity.Project;
import com.mindera.finalproject.be.exception.course.CourseNotFoundException;
import com.mindera.finalproject.be.exception.project.ProjectNotFoundException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.impl.CourseServiceImpl;
import com.mindera.finalproject.be.service.impl.PersonServiceImpl;
import com.mindera.finalproject.be.service.impl.ProjectServiceImpl;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.mindera.finalproject.be.messages.Messages.PROJECT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProjectServiceTests {

    private final String id = "PROJECT#1";

    private final String PROJECT = "PROJECT#";

    List<String> studentsId = Arrays.asList("PERSON#ac271736-2fdc-4a5a-a32d-031bae808dfe", "PERSON#ac271736-2fdc-4a5a-a32d-031bae808AAA");
    @InjectMocks
    ProjectServiceImpl projectService;

    @Mock
    private PersonServiceImpl personService;

    @Mock
    private CourseServiceImpl courseService;

    @Mock
    private DynamoDbTable<Project> projectTable;

    private PersonPublicDto getPersonPublicDto() {
        return new PersonPublicDto(
                "PERSON#ac271736-2fdc-4a5a-a32d-031bae808dfe",
                "TestEmail",
                "TestFN",
                "TestLN",
                "TestRole",
                "TestUS",
                LocalDate.now(),
                "TestAddress"
        );
    }
    private PersonPublicDto teacher() {
        return new PersonPublicDto(
                "PERSON#ac271736-2fdc-4a5a-a32d-031bae808AAA",
                "TestEmail",
                "TestFN",
                "TestLN",
                "Teacher",
                "TestUS",
                LocalDate.now(),
                "TestAddress"
        );
    }

    private CoursePublicDto getCoursePublicDto() {

        return new CoursePublicDto(
                "COURSE#c75e7c3b-b422-4c41-a50b-c7fa8bce165c",
                "TestName",
                1,
                teacher(),
                "TestSyllabus",
                "TestProgram",
                "TestSchedule",
                new BigDecimal("100"),
                30,
                "LOCATION"

        );
    }

    private Project getProject() {
        Project project = new Project();
        project.setPK("PROJECT#");
        project.setSK(id);
        project.setStudents(studentsId);
        project.setCourseId("COURSE#c75e7c3b-b422-4c41-a50b-c7fa8bce165c");
        project.setName("TestName");
        project.setGitHubRepo("TestGitHubRepo");
        project.setActive(true);
        return project;
    }

    @Test
    void testGetAll() {
        List<Project> project = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            project.add(getProject());
        }
        Page<Project> page1 = Page.create(project.subList(0, 100));
        Page<Project> page2 = Page.create(project.subList(100, 101));

        SdkIterable<Page<Project>> projects = () -> List.of(page1, page2).iterator();

        PageIterable<Project> pageIterable = PageIterable.create(projects);

        when(projectTable.query(any(QueryEnhancedRequest.class))).thenReturn(pageIterable);

        List<ProjectPublicDto> result = projectService.getAll(0, 100);
        List<ProjectPublicDto> result2 = projectService.getAll(1, 100);

        verify(projectTable, times(2)).query(any(QueryEnhancedRequest.class));
        assertEquals(100, result.size());
        assertEquals(1, result2.size());
    }
    @Test
    void testGetById() throws Exception {

        Project project = getProject();
        when(personService.getById(anyString())).thenReturn(getPersonPublicDto());
        when(courseService.getById(anyString())).thenReturn(getCoursePublicDto());
        when(projectTable.getItem(any(Key.class))).thenReturn(project);

        ProjectPublicDto result = projectService.getById(id);

        verify(personService, times(2)).getById(anyString());
        verify(courseService, times(1)).getById(anyString());
        verify(projectTable, times(1)).getItem(any(Key.class));

        assertNotNull(result);
        assertEquals(project.getSK(), result.id());
    }

    @Test
    void testGetByIdProjectNotFound() {

        when(projectTable.getItem(any(Key.class))).thenReturn(null);
        try {
            projectService.getById(id);
        } catch (Exception e) {
            assertNotNull(PROJECT_NOT_FOUND + id, e.getMessage());
        }
    }
    @Test
    void testCreateProject() throws ProjectNotFoundException, PersonNotFoundException, CourseNotFoundException {

        ProjectCreateDto projectCreateDto = new ProjectCreateDto("name", studentsId, "COURSE#c75e7c3b-b422-4c41-a50b-c7fa8bce165c", "https://github.com/user/repo");

        ProjectPublicDto result = projectService.create(projectCreateDto);

        assertNotNull(result);
        assertEquals(projectCreateDto.name(), result.name());
      // assertEquals(projectCreateDto.studentIds(), result.students());
      //  assertEquals(projectCreateDto.courseId(), result.course().id());
        assertEquals(projectCreateDto.gitHubRepo(), result.gitHubRepo());
    }
    @Test
    void testUpdateProject() throws ProjectNotFoundException, PersonNotFoundException, CourseNotFoundException {
        String id = PROJECT + UUID.randomUUID();
        Project project = new Project();
        project.setPK(PROJECT);
        project.setSK(id);
        project.setStudents(studentsId);
        project.setCourseId("COURSE#c75e7c3b-b422-4c41-a50b-c7fa8bce165c");
        Key key = Key.builder().partitionValue(PROJECT).sortValue(id).build();

        when(projectTable.getItem(key)).thenReturn(project);

        ProjectPublicDto result = projectService.getById(id);

        assertNotNull(result);
        assertEquals(project.getSK(), result.id());
    }

    @Test
    void testDeleteProject() throws ProjectNotFoundException, PersonNotFoundException, CourseNotFoundException {
        String id = PROJECT + UUID.randomUUID();
        Project project = new Project();
        project.setPK(PROJECT);
        project.setSK(id);
        Key key = Key.builder().partitionValue(PROJECT).sortValue(id).build();

        when(projectTable.getItem(key)).thenReturn(project);

        projectService.delete(id);

        verify(projectTable, times(1)).updateItem(project);
    }

    @Test
    void testDeleteProjectNotFound() throws ProjectNotFoundException, PersonNotFoundException, CourseNotFoundException {
        String id = "IDNotExistent";

        when(projectTable.getItem(any(Key.class))).thenReturn(null);

        assertThrows(ProjectNotFoundException.class, () -> projectService.delete(id));

        verify(projectTable, never()).updateItem(any(Project.class));
    }

}

