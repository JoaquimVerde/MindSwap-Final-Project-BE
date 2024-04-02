/*
package com.mindera.finalproject.be.converter;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Project;

import java.util.List;

public class ProjectConverter {

    public static ProjectCreateDto convertFromEntityToDto(Project project) {
        return new ProjectCreateDto(
                project.getName(),
                project.getCourseId(),
                project.getGitHubRepo()
        );
    }

    public static Project convertFromDtoToEntity(ProjectCreateDto projectCreateDto, List<Person> students) {
        return new Project(
                null,
                students,
                projectCreateDto.courseId(),
                projectCreateDto.name(),
                projectCreateDto.gitHubRepo(),
                0
        );
    }
}
*/
