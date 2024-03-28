package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.dto.student.StudentCreateDto;
import com.mindera.finalproject.be.dto.student.StudentPublicDto;
import com.mindera.finalproject.be.entity.Student;
import com.mindera.finalproject.be.repository.StudentRepository;
import com.mindera.finalproject.be.service.StudentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
@ApplicationScoped
public class StudentServiceImpl implements StudentService {

    @Inject
    StudentRepository studentRepository;
    @Override
    public List<Student> findAll() {
        return null;
    }

    @Override
    public StudentPublicDto add(StudentCreateDto studentCreateDto) {
        return null;
    }

    @Override
    public StudentPublicDto get(Long id) {
        return null;
    }

    @Override
    public StudentPublicDto edit(Long id, StudentCreateDto studentCreateDto) {
        return null;
    }

    @Override
    public StudentPublicDto delete(Long id) {
        return null;
    }
}
