package com.mindera.finalproject.be.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@DynamoDbBean
public class Course {

    private String PK;
    private String SK;
    private String name;
    private Integer edition;
    private String teacherId;
    private String syllabus;
    private String program;
    private String schedule;
    private BigDecimal price;
    private Integer duration;
    private String location;
    private Boolean active;
    private Integer numberOfApplications;
    private Integer maxNumberOfApplications;

    public Course() {
    }

    public Course(String name, Integer edition, String teacherId, String syllabus, String program, String schedule, BigDecimal price, Integer duration, String location) {
        this.name = name;
        this.edition = edition;
        this.teacherId = teacherId;
        this.syllabus = syllabus;
        this.program = program;
        this.schedule = schedule;
        this.price = price;
        this.duration = duration;
        this.location = location;
        this.active = true;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    @DynamoDbSecondarySortKey(indexNames = {"GSIPK"})
    public String getPK() {
        return PK;
    }
    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSK() {
        return SK;
    }

    public void setPK(String pk) {
        this.PK = pk;
    }

    public void setSK(String sk) {
        this.SK = sk;
    }

    @DynamoDbAttribute("Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDbAttribute("Edition")
    public Integer getEdition() {
        return edition;
    }

    public void setEdition(Integer edition) {
        this.edition = edition;
    }

    @DynamoDbAttribute("TeacherId")
    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacher) {
        this.teacherId = teacher;
    }

    @DynamoDbAttribute("Syllabus")
    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    @DynamoDbAttribute("Program")
    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    @DynamoDbAttribute("Schedule")
    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @DynamoDbAttribute("Price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @DynamoDbAttribute("Duration")
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @DynamoDbAttribute("Location")
    @DynamoDbSecondaryPartitionKey(indexNames = {"GSIPK"})
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @DynamoDbAttribute("NumberOfApplications")
    public Integer getNumberOfApplications() {
        return numberOfApplications;
    }

    public void setNumberOfApplications(Integer numberOfApplications) {
        this.numberOfApplications = numberOfApplications;
    }

    @DynamoDbAttribute("MaxNumberOfApplications")
    public Integer getMaxNumberOfApplications() {
        return maxNumberOfApplications;
    }

    public void setMaxNumberOfApplications(Integer maxNumberOfApplications) {
        this.maxNumberOfApplications = maxNumberOfApplications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(PK, course.PK) && Objects.equals(SK, course.SK) && Objects.equals(name, course.name) && Objects.equals(edition, course.edition) && Objects.equals(teacherId, course.teacherId) && Objects.equals(syllabus, course.syllabus) && Objects.equals(program, course.program) && Objects.equals(schedule, course.schedule) && Objects.equals(price, course.price) && Objects.equals(duration, course.duration) && Objects.equals(location, course.location) && Objects.equals(active, course.active) && Objects.equals(numberOfApplications, course.numberOfApplications) && Objects.equals(maxNumberOfApplications, course.maxNumberOfApplications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(PK, SK, name, edition, teacherId, syllabus, program, schedule, price, duration, location, active, numberOfApplications, maxNumberOfApplications);
    }

    @Override
    public String toString() {
        return "Course{" +
                "PK='" + PK + '\'' +
                ", SK='" + SK + '\'' +
                ", name='" + name + '\'' +
                ", edition=" + edition +
                ", teacherId='" + teacherId + '\'' +
                ", syllabus='" + syllabus + '\'' +
                ", program='" + program + '\'' +
                ", schedule='" + schedule + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", location='" + location + '\'' +
                ", active=" + active +
                ", numberOfApplications=" + numberOfApplications +
                ", maxNumberOfApplications=" + maxNumberOfApplications +
                '}';
    }
}
