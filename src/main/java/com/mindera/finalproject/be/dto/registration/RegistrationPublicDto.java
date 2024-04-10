package com.mindera.finalproject.be.dto.registration;

import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;

public record RegistrationPublicDto(
        String id,
        PersonPublicDto student,
        CoursePublicDto course,
        String status,
        Integer finalGrade,
        Boolean active,
        String aboutYou,
        Boolean prevKnowledge,
        Boolean prevExperience
) {
}
