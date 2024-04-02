package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.converter.ProjectConverter;
import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Project;
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

    private final String TABLE_NAME = "Project";
    private DynamoDbTable<Project> projectTable;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        projectTable = dynamoEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Project.class));
    }

    @Override
    public List<ProjectPublicDto> getAll() {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(s -> s.partitionValue("PROJECT#"));
        SdkIterable<Page<Project>> projects = projectTable.query(queryConditional);
        List<Project> projectsList = new ArrayList<>();
        projects.forEach(page -> projectsList.addAll(page.items()));
        return projectsList.stream().map(ProjectConverter::fromEntityToPublicDto).collect(Collectors.toList());
    }

    @Override
    public ProjectPublicDto getById(String id) {
        Project project = projectTable.getItem(Key.builder().partitionValue(id).build());
        return ProjectConverter.fromEntityToPublicDto(project);
    }

    @Override
    public ProjectPublicDto create(ProjectCreateDto projectCreateDto) {
        String id = UUID.randomUUID().toString();
        Project project = ProjectConverter.convertFromDtoToEntity(projectCreateDto);
        project.setPK(id);
        project.setSK(id);
        projectTable.putItem(project);
        return ProjectConverter.fromEntityToPublicDto(project);
    }
    @Override
    public ProjectPublicDto update(String id, ProjectCreateDto projectCreateDto) {
        Project project = projectTable.getItem(Key.builder().partitionValue(id).build());
        project.setName(projectCreateDto.name());
        project.setStudents(projectCreateDto.studentIds());
        project.setCourseId(projectCreateDto.courseId());
        project.setGitHubRepo(projectCreateDto.gitHubRepo());
        projectTable.updateItem(project);
        return ProjectConverter.fromEntityToPublicDto(project);
    }

    @Override
    public void delete(String id) {
        projectTable.deleteItem(Key.builder().partitionValue(id).build());
    }
}
