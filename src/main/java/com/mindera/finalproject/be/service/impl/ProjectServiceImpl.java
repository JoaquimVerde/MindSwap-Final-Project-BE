package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.entity.Project;
import com.mindera.finalproject.be.service.ProjectService;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjectServiceImpl implements ProjectService {

    private DynamoDbTable<Project> projectTable;

    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        projectTable = dynamoEnhancedClient.table("Project", TableSchema.fromBean(Project.class));
    }

    @Override
    public List<Project> findAll() {
        return projectTable.scan().items().stream().collect(Collectors.toList());
    }

    @Override
    public Project findById(Long id) {
        return projectTable.getItem(Key.builder().partitionValue(id).build());
    }

    @Override
    public Project create(ProjectCreateDto projectCreateDto) {
        Project project = new Project();
        project.setName("teste1");
        project.setGitHubRepo("http://github.com/teste1");
        projectTable.putItem(project);
        return project;
    }

    @Override
    public Project update(Long id, ProjectCreateDto projectCreateDto) {
        Project project = projectTable.getItem(Key.builder().partitionValue(id).build());
        project.setName(projectCreateDto.name());
        project.setGitHubRepo(projectCreateDto.gitHubRepo());
        projectTable.putItem(project);
        return project;
    }

    @Override
    public void delete(Long id) {
        projectTable.deleteItem(Key.builder().partitionValue(id).build());
    }
}
