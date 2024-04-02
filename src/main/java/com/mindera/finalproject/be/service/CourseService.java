package com.mindera.finalproject.be.service;


import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.entity.Course;

import java.util.List;

public interface CourseService {

    List<CoursePublicDto> getAll();
    CoursePublicDto getById(String id);
    List<Course> getByLocation(String location);
    CoursePublicDto create(CourseCreateDto courseCreateDto);
    CoursePublicDto update(String id, CourseCreateDto coursePublicDto);
    void delete(String id);
}
