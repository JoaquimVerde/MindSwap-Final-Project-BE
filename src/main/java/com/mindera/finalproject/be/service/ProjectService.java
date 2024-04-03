package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;
import com.mindera.finalproject.be.exception.project.ProjectNotFoundException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;

import java.util.List;


public interface ProjectService {

    List<ProjectPublicDto> getAll();

    ProjectPublicDto getById(String id) throws ProjectNotFoundException;

    ProjectPublicDto create(ProjectCreateDto projectCreateDto) throws ProjectNotFoundException, PersonNotFoundException;

    ProjectPublicDto update(String id, ProjectCreateDto projectCreateDto);

    void delete(String id);

}
