package com.mindera.finalproject.be.converter;

import com.mindera.finalproject.be.dto.person.PersonCreateDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Person;

import java.util.List;

public class PersonConverter {

    public static PersonPublicDto fromEntityToPublicDto(Person person) {
        return new PersonPublicDto(
                person.getSK(),
                person.getEmail(),
                person.getFirstName(),
                person.getLastName(),
                person.getRole(),
                person.getUsername(),
                person.getDateOfBirth(),
                person.getAddress(),
                person.getCurriculum()
        );
    }

    public static List<PersonPublicDto> fromEntityToPublicDtoList(List<Person> persons) {
        return persons.stream().map(PersonConverter::fromEntityToPublicDto).toList();
    }

    public static Person fromCreateDtoToEntity(PersonCreateDto personCreateDto) {
        return new Person(
                personCreateDto.email(),
                personCreateDto.firstName(),
                personCreateDto.lastName(),
                personCreateDto.role(),
                personCreateDto.username(),
                personCreateDto.dateOfBirth(),
                personCreateDto.address(),
                personCreateDto.cv()
        );
    }
}
