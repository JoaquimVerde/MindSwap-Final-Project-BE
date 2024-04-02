package com.mindera.finalproject.be.dto.registration;

public record RegistrationPublicDto(
        
        String PK,
        String status,
        String finalGrade,
        Boolean active,
        String aboutYou,
        Boolean prevKnowledge,
        Boolean prevExperience
) {
}
