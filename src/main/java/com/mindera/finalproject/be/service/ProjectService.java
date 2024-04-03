package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;
import com.mindera.finalproject.be.entity.Project;
import com.mindera.finalproject.be.exception.project.ProjectNotFoundException;

import java.util.List;


public interface ProjectService {

    List<ProjectPublicDto> getAll() throws ProjectNotFoundException;
    ProjectPublicDto getById(String id) throws ProjectNotFoundException;
    ProjectPublicDto create(ProjectCreateDto projectCreateDto) throws ProjectNotFoundException;
    ProjectPublicDto update(String id, ProjectCreateDto projectCreateDto);
    void delete(String id);

}
