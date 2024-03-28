package com.mindera.finalproject.be.repository;

import com.mindera.finalproject.be.entity.Project;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.List;

import static com.mindera.finalproject.be.config.DynamoDbConfig.client;
import static com.mindera.finalproject.be.repository.schema.TableSchemas.ProjectTableSchema;


@ApplicationScoped
public class ProjectRepository {

    DynamoDbTable<Project> projectTable = client.table("Project", ProjectTableSchema);

    public ProjectRepository() {
        projectTable.createTable();
    }

    public void save(Project project) {
        projectTable.putItem(project);
    }

    Project findById(Long id) {
        return projectTable.getItem(Key.builder().partitionValue(id).build());
    }

    public List<Project> findAll() {
        return projectTable.scan().items().stream().toList();
    }

    public void update(Project project) {
        projectTable.updateItem(project);
    }

    public void delete(Long id) {
        projectTable.deleteItem(Key.builder().partitionValue(id).build());
    }

}
