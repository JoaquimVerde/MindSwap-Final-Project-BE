package com.mindera.finalproject.be.converter;

import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Course;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class CourseConverter {

    public static CoursePublicDto fromEntityToPublicDto(Course course, PersonPublicDto teacher) {
        if (teacher == null) {
            return new CoursePublicDto(
                    course.getSK(),
                    course.getName(),
                    course.getEdition(),
                    null,
                    course.getSyllabus(),
                    course.getProgram(),
                    course.getSchedule(),
                    course.getPrice(),
                    course.getDuration(),
                    course.getLocation(),
                    course.getMaxStudents()
            );
        }
        return new CoursePublicDto(
                course.getSK(),
                course.getName(),
                course.getEdition(),
                teacher,
                course.getSyllabus(),
                course.getProgram(),
                course.getSchedule(),
                course.getPrice(),
                course.getDuration(),
                course.getLocation(),
                course.getMaxStudents()
        );
    }

    public static Course fromCreateDtoToEntity(CourseCreateDto courseCreateDto) {

        return new Course(
                courseCreateDto.name(),
                courseCreateDto.edition(),
                courseCreateDto.teacherId(),
                courseCreateDto.syllabus(),
                courseCreateDto.program(),
                courseCreateDto.schedule(),
                courseCreateDto.price(),
                courseCreateDto.duration(),
                courseCreateDto.location()
        );
    }
}
