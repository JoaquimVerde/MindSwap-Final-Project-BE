package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;
import com.mindera.finalproject.be.entity.Project;
import com.mindera.finalproject.be.repository.ProjectRepository;
import com.mindera.finalproject.be.service.ProjectService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProjectServiceImpl implements ProjectService {


    @Inject
    ProjectRepository projectRepository;
    @Override
    public List<Project> findAll() {
        Optional<List<Project>> projects = Optional.of(projectRepository.findAll());

        if (projects.isEmpty()) {
            return null;
        }
        return projects.get();
    }

    @Override
    public ProjectPublicDto findById(Long id) {
        return null;
    }

    @Override
    public ProjectCreateDto create(ProjectCreateDto ProjectCreateDto) {
        return null;
    }

    @Override
    public ProjectPublicDto update(Long id, ProjectCreateDto ProjectCreateDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
