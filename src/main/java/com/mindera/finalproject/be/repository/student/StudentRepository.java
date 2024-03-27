package com.mindera.finalproject.be.repository.student;

import com.mindera.finalproject.be.entity.Student;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.awt.print.Pageable;
import java.util.Optional;

public interface StudentRepository {
    Student save (Student student);
    Page<Student> findAll(Pageable pageable);
    Page<Student> findByName(String name, Pageable pageable);
    Optional<Student> findById(Long id);
    void delete(Long id);

}
