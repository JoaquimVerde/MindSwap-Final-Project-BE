package com.mindera.finalproject.be.service;


import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.exception.course.CourseAlreadyExistsException;
import com.mindera.finalproject.be.exception.course.CourseNotFoundException;
import com.mindera.finalproject.be.exception.student.PersonNotATeacherException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;

import java.util.List;

public interface CourseService {

    List<CoursePublicDto> getAll(Integer page, Integer limit);

    CoursePublicDto getById(String id) throws PersonNotFoundException, CourseNotFoundException;

    List<CoursePublicDto> getByLocation(String location, Integer page, Integer limit);

    CoursePublicDto create(CourseCreateDto courseCreateDto) throws PersonNotFoundException, CourseAlreadyExistsException, PersonNotATeacherException;

    CoursePublicDto update(String id, CourseCreateDto coursePublicDto) throws PersonNotFoundException, CourseNotFoundException, PersonNotATeacherException;

    void delete(String id) throws CourseNotFoundException;

    Course findById(String id) throws CourseNotFoundException;
}
