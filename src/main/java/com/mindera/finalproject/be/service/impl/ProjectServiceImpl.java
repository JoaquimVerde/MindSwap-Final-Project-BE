package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.converter.ProjectConverter;
import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Project;
import com.mindera.finalproject.be.exception.project.ProjectNotFoundException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.CourseService;
import com.mindera.finalproject.be.service.PersonService;
import com.mindera.finalproject.be.service.ProjectService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ProjectServiceImpl implements ProjectService {

    private final String TABLE_PROJECT = "Project";
    private final String PROJECT = "PROJECT#";
    private DynamoDbTable<Project> projectTable;

    @Inject
    private CourseService courseService;

    @Inject
    private PersonService personService;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        projectTable = dynamoEnhancedClient.table(TABLE_PROJECT, TableSchema.fromBean(Project.class));

    }

    @Override
    public List<ProjectPublicDto> getAll() {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(s -> s.partitionValue(PROJECT).sortValue(PROJECT));
        SdkIterable<Page<Project>> projects = projectTable.query(queryConditional);
        List<Project> projectsList = new ArrayList<>();
        projects.forEach(page -> projectsList.addAll(page.items()));
        return projectsList.stream().filter(Project::getActive).map(ProjectConverter::fromEntityToPublicDto).toList();
    }

    @Override
    public ProjectPublicDto getById(String id) throws ProjectNotFoundException {
        Project project = projectTable.getItem(Key.builder().partitionValue(PROJECT).sortValue(id).build());
        if (project == null) {
            throw new ProjectNotFoundException("Project not found");
        }
        return ProjectConverter.fromEntityToPublicDto(project);
    }

    @Override
    public ProjectPublicDto create(ProjectCreateDto projectCreateDto) throws ProjectNotFoundException, PersonNotFoundException {
        Project project = ProjectConverter.convertFromDtoToEntity(projectCreateDto);
        Course course = courseService.findById(projectCreateDto.courseId());
        if (course == null) {
            return null; // throw exception
        }
        List<String> studentIds = projectCreateDto.studentIds();
        List<String> studentsNotExists = new ArrayList<>();
        for (String studentId : studentIds) {
            Person student = personService.findById(studentId);
            if (student == null) {
                studentsNotExists.add(studentId);
            }
        }
        if (!studentsNotExists.isEmpty()) {
            throw new ProjectNotFoundException("Students with ids " + studentsNotExists + " not found");
        }
        project.setPK(PROJECT);
        project.setSK(PROJECT + UUID.randomUUID());
        projectTable.putItem(project);
        return ProjectConverter.fromEntityToPublicDto(project);
    }

    @Override
    public ProjectPublicDto update(String id, ProjectCreateDto projectCreateDto) {
        Project project = projectTable.getItem(Key.builder().partitionValue(PROJECT).sortValue(id).build());
        if (project == null) {
            return null; // throw exception
        }
        project.setName(projectCreateDto.name());
        project.setStudents(projectCreateDto.studentIds());
        project.setCourseId(projectCreateDto.courseId());
        project.setGitHubRepo(projectCreateDto.gitHubRepo());
        projectTable.updateItem(project);
        return ProjectConverter.fromEntityToPublicDto(project);
    }

    @Override
    public void delete(String id) {
        Project project = projectTable.getItem(Key.builder().partitionValue(PROJECT).sortValue(id).build());
        if (project == null) {
            return; // throw exception
        }
        project.setActive(false);
        projectTable.updateItem(project);
    }
}
