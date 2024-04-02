package com.mindera.finalproject.be.service;


import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.entity.Course;

import java.util.List;

public interface CourseService {

    List<Course> getAll();
    Course getById(String id);
    Course create(CourseCreateDto coursePublicDto);
    Course update(String id, CourseCreateDto coursePublicDto);
    void delete(String id);
}
