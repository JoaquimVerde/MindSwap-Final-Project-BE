package com.mindera.finalproject.be.service;


import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;

import java.util.List;

public interface CourseService {

    List<CoursePublicDto> getAll();
    CoursePublicDto getById(String id);
    CoursePublicDto create(CourseCreateDto coursePublicDto);
    CoursePublicDto update(String id, CourseCreateDto coursePublicDto);
    void delete(String id);
}
