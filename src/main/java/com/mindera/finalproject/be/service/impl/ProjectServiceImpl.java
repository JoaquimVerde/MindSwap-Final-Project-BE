package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.entity.Person;
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
    public List<Project> getAll() {
        return projectTable.scan().items().stream().collect(Collectors.toList());
    }

    @Override
    public Project getById(String id) {
        return projectTable.getItem(Key.builder().partitionValue(id).build());
    }

    @Override
    public Project create(ProjectCreateDto  dto) {
        return null;
    }
    @Override
    public Project update(String id, ProjectCreateDto projectCreateDto) {
        return null;
    }

    @Override
    public void delete(String id) {
        projectTable.deleteItem(Key.builder().partitionValue(id).build());
    }
}
