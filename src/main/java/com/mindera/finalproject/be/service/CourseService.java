package com.mindera.finalproject.be.service;


import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;

import java.util.List;

public interface CourseService {

    List<CoursePublicDto> getAll();

    CoursePublicDto getById(String id) throws PersonNotFoundException;

    List<CoursePublicDto> getByLocation(String location);

    CoursePublicDto create(CourseCreateDto courseCreateDto) throws PersonNotFoundException;

    CoursePublicDto update(String id, CourseCreateDto coursePublicDto) throws PersonNotFoundException;

    void delete(String id);

    Course findById(String id);
}
