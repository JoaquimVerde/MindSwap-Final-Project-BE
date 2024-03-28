package com.mindera.finalproject.be.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class Registration {

    private String registrationId;
    private Long personId;
    private Long courseId;
    private String status;
    private String finalGrade;
    private Boolean active;
    private String compositeKey;

    public Registration() {
    }

    public Registration(String registrationId,
                        Long personId,
                        Long courseId,
                        String status,
                        String finalGrade,
                        Boolean active) {
        this.registrationId = registrationId;
        this.personId = personId;
        this.courseId = courseId;
        this.status = status;
        this.finalGrade = finalGrade;
        this.active = active;
        this.compositeKey = personId + "#" + courseId;
    }


    public String getRegistrationId() {
        return registrationId;
    }

    public Long getPersonId() {
        return personId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getStatus() {
        return status;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public Boolean getActive() {
        return active;
    }


    public String getCompositeKey() {
        return compositeKey;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setCompositeKey(String compositeKey) {
            this.compositeKey = compositeKey;
        }
}