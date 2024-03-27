package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;

public interface ProjectService {

    ProjectPublicDto findAll();
    ProjectPublicDto findById(Long id);
    ProjectCreateDto create(ProjectCreateDto ProjectCreateDto);
    ProjectPublicDto update(Long id, ProjectCreateDto ProjectCreateDto);
    void delete(Long id);

}
