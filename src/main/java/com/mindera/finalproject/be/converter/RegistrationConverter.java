package com.mindera.finalproject.be.converter;

import com.mindera.finalproject.be.dto.registration.RegistrationCreateDto;
import com.mindera.finalproject.be.dto.registration.RegistrationPublicDto;
import com.mindera.finalproject.be.entity.Registration;

import java.util.List;

public class RegistrationConverter {

    public static RegistrationPublicDto fromEntityToPublicDto(Registration registration) {
        return new RegistrationPublicDto(
                registration.getPK(),
                registration.getStatus(),
                registration.getFinalGrade(),
                registration.getActive(),
                registration.getAboutYou(),
                registration.getPrevKnowledge(),
                registration.getPrevExperience()
        );
    }

    public static List<RegistrationPublicDto> fromEntityListToPublicDtoList(List<Registration> registrations) {
        return registrations.stream().map(RegistrationConverter::fromEntityToPublicDto).toList();
    }

    public static  Registration fromCreateDtoToEntity(RegistrationCreateDto registrationCreateDto) {
        return new Registration(
                registrationCreateDto.personId(),
                registrationCreateDto.courseId(),
                registrationCreateDto.status(),
                registrationCreateDto.finalGrade(),
                registrationCreateDto.active(),
                registrationCreateDto.aboutYou(),
                registrationCreateDto.prevKnowledge(),
                registrationCreateDto.prevExperience()
        );
    }

}
