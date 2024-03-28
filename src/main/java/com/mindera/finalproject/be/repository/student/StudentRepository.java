package com.mindera.finalproject.be.repository.student;

import com.mindera.finalproject.be.entity.Student;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;


import java.time.LocalDate;
import java.util.List;

import static com.mindera.finalproject.be.repository.schema.TablesSchema.studentTableSchema;
import static com.mindera.finalproject.be.repository.student.DbConfig.enhancedClient;

@ApplicationScoped
public class StudentRepository{
    DynamoDbTable<Student> table = enhancedClient.table("StudentTable", studentTableSchema);

    public StudentRepository() {
        table.createTable();
    }

    public void save(Student student) {
        table.putItem(student);
    }

    public List<Student> getAll() {
        return table.scan().items().stream().toList();
    }

    public Student getById(Long id){
       return table.getItem(Key.builder().partitionValue(id).build());
    }
    public void update(Student student){
         table.updateItem(student);
    }
    public void delete(Long id){
        table.deleteItem(Key.builder().partitionValue(id).build());
    }

}

