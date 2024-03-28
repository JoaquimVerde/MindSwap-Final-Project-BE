package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.repository.CourseRepository;
import com.mindera.finalproject.be.service.CourseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CourseServiceImpl implements CourseService {

    @Inject
    CourseRepository courseRepository;

    @Override
    public List<CoursePublicDto> findAll() {
        return null;
    }

    @Override
    public CoursePublicDto findById(Long id) {
        return null;
    }

    @Override
    public CoursePublicDto create(CourseCreateDto courseCreateDtoDto) {
        return null;
    }

    @Override
    public CoursePublicDto edit(Long id, CourseCreateDto courseCreateDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
