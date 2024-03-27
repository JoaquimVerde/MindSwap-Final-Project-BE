package com.mindera.finalproject.be.service;


import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.entity.Course;

import java.util.List;

public interface CourseService {

    List<Course> findAll();
    CoursePublicDto findById(Long id);
    CoursePublicDto create(CourseCreateDto coursePublicDto);
    CoursePublicDto edit(Long id, CourseCreateDto coursePublicDto);
    String delete(Long id);
}
