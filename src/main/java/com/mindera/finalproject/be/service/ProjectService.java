package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;

public interface ProjectService {

    ProjectPublicDto findAll();
    ProjectPublicDto findById(Long id);
    ProjectCreateDto createProject(ProjectCreateDto ProjectCreateDto);

    //PUT

    // DELETE

}
