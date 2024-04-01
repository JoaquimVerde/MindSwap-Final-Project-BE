package com.mindera.finalproject.be.Project;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.entity.Project;
import com.mindera.finalproject.be.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProjectServiceTests {

    @InjectMocks
    ProjectServiceImpl projectService;



    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testFindAll() {
        List<Project> projects = projectService.findAll();
        assertFalse(projects.isEmpty());
    }

    @Test
     void testFindById() {
        Project project = projectService.findById(1L);
        assertEquals(1L, project.getId());
    }

    @Test
     void testCreateProject() {
        ProjectCreateDto projectCreateDto = new ProjectCreateDto("name", "gitHubRepo");
        Project project = projectService.create(projectCreateDto);
        assertEquals("name", project.getName());
        assertEquals("gitHubRepo", project.getGitHubRepo());
    }

    @Test
     void testUpdateProject() {
        ProjectCreateDto projectCreateDto = new ProjectCreateDto("name", "gitHubRepo");
        Project project = projectService.update(1L, projectCreateDto);
        assertEquals("name", project.getName());
        assertEquals("gitHubRepo", project.getGitHubRepo());
    }

    @Test
     void testDeleteProject() {
        projectService.delete(1L);
    }
}
