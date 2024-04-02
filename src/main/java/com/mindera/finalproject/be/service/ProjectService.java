package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;
import com.mindera.finalproject.be.entity.Project;

import java.util.List;


public interface ProjectService {

    List<ProjectPublicDto> getAll() ;
    ProjectPublicDto getById(String id);
    ProjectPublicDto create(ProjectCreateDto projectCreateDto);
    ProjectPublicDto update(String id, ProjectCreateDto projectCreateDto);
    void delete(String id);

}
