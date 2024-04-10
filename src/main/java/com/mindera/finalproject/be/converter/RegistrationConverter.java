package com.mindera.finalproject.be.converter;

import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.entity.Registration;

public class RegistrationConverter {

    public static RegistrationPublicDto fromEntityToPublicDto(Registration registration, PersonPublicDto student, CoursePublicDto course) {
        return new RegistrationPublicDto(
                registration.getSK(),
                student,
                course,
                registration.getStatus(),
                registration.getFinalGrade(),
                registration.getActive(),
                registration.getAboutYou(),
                registration.getPrevKnowledge(),
                registration.getPrevExperience()
        );
    }

    /*public static List<RegistrationPublicDto> fromEntityListToPublicDtoList(List<Registration> registrations) {
        return registrations.stream().map(registration -> {
            Person student = registration.getPerson();
            Course course = null;
            return fromEntityToPublicDto(registration, student, course);
        }).toList();
    }*/

    public static Registration fromCreateDtoToEntity(RegistrationCreateDto registrationCreateDto) {
        return new Registration(
                registrationCreateDto.personId(),
                registrationCreateDto.courseId(),
                registrationCreateDto.status(),
                registrationCreateDto.finalGrade(),
                registrationCreateDto.aboutYou(),
                registrationCreateDto.prevKnowledge(),
                registrationCreateDto.prevExperience()
        );
    }

}
