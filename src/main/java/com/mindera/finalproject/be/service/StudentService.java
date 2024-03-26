package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.student.StudentCreateDto;
import com.mindera.finalproject.be.dto.student.StudentPublicDto;
import com.mindera.finalproject.be.entity.Student;

import java.net.URI;
import java.util.List;

public interface StudentService {
    List<Student> findAll();

    StudentPublicDto add(StudentCreateDto studentCreateDto);

    StudentPublicDto get(Long id);
}
