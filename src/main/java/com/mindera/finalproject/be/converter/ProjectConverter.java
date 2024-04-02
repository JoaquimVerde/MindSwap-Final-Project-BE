package com.mindera.finalproject.be.converter;

import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Project;

import java.util.List;

public class ProjectConverter {

    public static ProjectCreateDto convertFromEntityToDto(Project project) {
        return new ProjectCreateDto(
                project.getName(),
                project.getStudents(),
                project.getCourseId(),
                project.getGitHubRepo()
        );
    }

    public static Project convertFromDtoToEntity(ProjectCreateDto projectCreateDto) {
        return new Project(
                projectCreateDto.studentIds(),
                projectCreateDto.name(),
                projectCreateDto.courseId(),
                projectCreateDto.gitHubRepo()
        );
    }


    public static ProjectPublicDto fromEntityToPublicDto(Project project) {
        return new ProjectPublicDto(
                project.getPK(),
                project.getName(),
                project.getStudents(),
                project.getCourseId(),
                project.getGitHubRepo(),
                project.getGrade()
        );
    }


    public static Project fromPublicDtoToEntity(ProjectPublicDto projectPublicDto) {
        return new Project(
                projectPublicDto.studentIds(),
                projectPublicDto.name(),
                projectPublicDto.courseId(),
                projectPublicDto.gitHubRepo()
        );
    }
}
