package com.mindera.finalproject.be.converter;

import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;
import com.mindera.finalproject.be.entity.Project;

import java.util.List;

public class ProjectConverter {

    public static Project convertFromDtoToEntity(ProjectCreateDto projectCreateDto) {
        return new Project(
                projectCreateDto.studentIds(),
                projectCreateDto.courseId(),
                projectCreateDto.name(),
                projectCreateDto.gitHubRepo()
        );
    }

    public static ProjectPublicDto fromEntityToPublicDto(Project project, CoursePublicDto course, List<PersonPublicDto> students) {
        return new ProjectPublicDto(
                project.getSK(),
                project.getName(),
                students,
                course,
                project.getGitHubRepo(),
                project.getGrade()
        );
    }
}
