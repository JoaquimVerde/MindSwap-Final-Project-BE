package com.mindera.finalproject.be.service;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.entity.Project;

import java.util.List;


public interface ProjectService {

    List<Project> getAll();
    Project getById(String id);
    Project create(ProjectCreateDto projectCreateDto);
    Project update(String id, ProjectCreateDto projectCreateDto);
    void delete(String id);

}
