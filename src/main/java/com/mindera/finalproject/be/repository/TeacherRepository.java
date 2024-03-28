package com.mindera.finalproject.be.repository;

import com.mindera.finalproject.be.entity.Teacher;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.List;

import static com.mindera.finalproject.be.config.DynamoDbConfig.client;
import static com.mindera.finalproject.be.repository.schema.TableSchemas.teacherTableSchema;

@ApplicationScoped
public class TeacherRepository {

    DynamoDbTable<Teacher> table = client.table("TeacherTable", teacherTableSchema);

    public TeacherRepository() {
        table.createTable();
    }

    public void save(Teacher teacher) {
        table.putItem(teacher);
    }

    public List<Teacher> getAll() {
        return table.scan().items().stream().toList();
    }

    public Teacher getById(Long id) {
        return table.getItem(Key.builder().partitionValue(id).build());
    }

    public void update(Teacher teacher) {
        table.updateItem(teacher);
    }

    public void delete(Long id) {
        table.deleteItem(Key.builder().partitionValue(id).build());
    }

}
