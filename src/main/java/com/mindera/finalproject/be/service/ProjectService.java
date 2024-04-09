package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;
import com.mindera.finalproject.be.dto.project.ProjectUpdateGradeDto;
import com.mindera.finalproject.be.exception.course.CourseNotFoundException;
import com.mindera.finalproject.be.exception.pdf.PdfException;
import com.mindera.finalproject.be.exception.project.ProjectNotFoundException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;

import java.util.List;


public interface ProjectService {

    List<ProjectPublicDto> getAll(Integer page, Integer limit) throws PdfException;

    ProjectPublicDto getById(String id) throws ProjectNotFoundException, PersonNotFoundException, CourseNotFoundException;

    ProjectPublicDto create(ProjectCreateDto projectCreateDto) throws ProjectNotFoundException, PersonNotFoundException, CourseNotFoundException;

    ProjectPublicDto update(String id, ProjectCreateDto projectCreateDto) throws PersonNotFoundException, ProjectNotFoundException, CourseNotFoundException;

    ProjectPublicDto updateGrade(String id, ProjectUpdateGradeDto projectUpdateGradeDto) throws ProjectNotFoundException, PersonNotFoundException, CourseNotFoundException;

    void delete(String id) throws ProjectNotFoundException;

    List<ProjectPublicDto> getProjectsByPersonId(String personId) throws PersonNotFoundException;

    List<ProjectPublicDto> getProjectsByCourseId(String courseId) throws CourseNotFoundException;
}
