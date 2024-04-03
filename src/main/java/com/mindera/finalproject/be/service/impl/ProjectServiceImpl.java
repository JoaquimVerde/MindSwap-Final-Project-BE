package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.converter.ProjectConverter;
import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Project;
import com.mindera.finalproject.be.exception.project.ProjectNotFoundException;
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
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjectServiceImpl implements ProjectService {

    private final String TABLE_PROJECT = "Project";
    private final String TABLE_PERSON = "Person";
    private final String TABLE_COURSE = "Course";
    private DynamoDbTable<Project> projectTable;
    private DynamoDbTable<Person> personTable;
    private DynamoDbTable<Course> courseTable;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        projectTable = dynamoEnhancedClient.table(TABLE_PROJECT, TableSchema.fromBean(Project.class));
        personTable = dynamoEnhancedClient.table(TABLE_PERSON, TableSchema.fromBean(Person.class));
        courseTable = dynamoEnhancedClient.table(TABLE_COURSE, TableSchema.fromBean(Course.class));
    }

    @Override
    public List<ProjectPublicDto> getAll() throws ProjectNotFoundException {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(s -> s.partitionValue("PROJECT#").sortValue("PROJECT#"));
        SdkIterable<Page<Project>> projects = projectTable.query(queryConditional);
        if(projects.stream().toList().isEmpty()) {
            throw new ProjectNotFoundException("Project not found");
        }
        List<Project> projectsList = new ArrayList<>();
        projects.forEach(page -> projectsList.addAll(page.items()));
        return projectsList.stream().filter(Project::getActive).map(ProjectConverter::fromEntityToPublicDto).toList();
    }

    @Override
    public ProjectPublicDto getById(String id) throws ProjectNotFoundException {
        Project project = projectTable.getItem(Key.builder().partitionValue(id).sortValue(id).build());
        if(project == null) {
            throw new ProjectNotFoundException("Project not found");
        }
        return ProjectConverter.fromEntityToPublicDto(project);
    }

    @Override
    public ProjectPublicDto create(ProjectCreateDto projectCreateDto) throws ProjectNotFoundException {
        String id = UUID.randomUUID().toString();
        Project project = ProjectConverter.convertFromDtoToEntity(projectCreateDto);
        Course course = courseTable.getItem(Key.builder().partitionValue(projectCreateDto.courseId()).build());
        if(course == null) {
            return null; // throw exception
        }
        List<String> studentIds = projectCreateDto.studentIds();
        List<String> studentsNotExists = new ArrayList<>();
        for(String studentId : studentIds) {
            Person student = personTable.getItem(Key.builder().partitionValue(studentId).build());
            if(student == null) {
                studentsNotExists.add(studentId);
            }
        }
        if(!studentsNotExists.isEmpty()) {
            throw new ProjectNotFoundException("Students with ids "+ studentsNotExists + " not found");
        }
        project.setPK(id);
        project.setSK(id);
        projectTable.putItem(project);
        return ProjectConverter.fromEntityToPublicDto(project);
    }
    @Override
    public ProjectPublicDto update(String id, ProjectCreateDto projectCreateDto) {
        Project project = projectTable.getItem(Key.builder().partitionValue(id).sortValue(id).build());
        if(project == null) {
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
        Project project = projectTable.getItem(Key.builder().partitionValue(id).sortValue(id).build());
        if(project == null) {
            return; // throw exception
        }
        project.setActive(false);
        projectTable.updateItem(project);
    }
}
