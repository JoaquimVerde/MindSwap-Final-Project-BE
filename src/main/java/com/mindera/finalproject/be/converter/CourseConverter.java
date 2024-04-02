package com.mindera.finalproject.be.converter;

import com.mindera.finalproject.be.dto.course.CourseCreateDto;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.entity.Course;

import java.util.List;

public class CourseConverter {

    public static CoursePublicDto fromEntityToPublicDto(Course course) {
        return new CoursePublicDto(
                course.getId(),
                course.getName(),
                course.getEdition(),
                course.getTeacher(),
                course.getSyllabus(),
                course.getProgram(),
                course.getSchedule(),
                course.getPrice(),
                course.getDuration(),
                course.getLocation()
        );
    }

    public static List<CoursePublicDto> fromEntityListToPublicDtoList(List<Course> courses) {
        return courses.stream().map(CourseConverter::fromEntityToPublicDto).toList();
    }

    public static Course fromCreateDtoToEntity(CourseCreateDto courseCreateDto) {

        return new Course(
                courseCreateDto.name(),
                courseCreateDto.edition(),
                courseCreateDto.teacher(),
                courseCreateDto.syllabus(),
                courseCreateDto.program(),
                courseCreateDto.schedule(),
                courseCreateDto.price(),
                courseCreateDto.duration(),
                courseCreateDto.location()
        );
    }
}
