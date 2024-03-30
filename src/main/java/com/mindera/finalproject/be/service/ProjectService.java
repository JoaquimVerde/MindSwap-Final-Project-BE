package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;
import com.mindera.finalproject.be.entity.Project;

import java.util.List;


public interface ProjectService {

    List<Project> findAll();
    Project findById(Long id);
    Project create(ProjectCreateDto projectCreateDto);
    Project update(Long id, ProjectCreateDto projectCreateDto);
    void delete(Long id);

}
