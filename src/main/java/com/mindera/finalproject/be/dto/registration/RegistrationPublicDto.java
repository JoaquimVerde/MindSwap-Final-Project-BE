package com.mindera.finalproject.be.dto.registration;

import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;

public record RegistrationPublicDto(
        
        Person student,
        Course course,
        String status,
        String finalGrade,
        Boolean active,
        String aboutYou,
        Boolean prevKnowledge,
        Boolean prevExperience
) {
}
