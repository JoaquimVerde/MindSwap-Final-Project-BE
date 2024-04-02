package com.mindera.finalproject.be.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@DynamoDbBean
public class Course {

    private String id;
    private String name;
    private Integer edition;
    private String teacherId;
    private String syllabus;
    private String program;
    private Map<String, String> schedule; //Can be something like {monday=10-18, tuesday=14-18}
    private BigDecimal price;
    private Integer duration; //I used Integer assuming days or months, but this can be changed
    private String location;
    private Integer numberOfApplications;
    private Integer maxNumberOfApplications;


    public Course() {
    }

    public Course(String name, Integer edition, String teacherId, String syllabus, String program, Map<String, String> schedule, BigDecimal price, Integer duration, String location) {
        this.name = name;
        this.edition = edition;
        this.teacherId = teacherId;
        this.syllabus = syllabus;
        this.program = program;
        this.schedule = schedule;
        this.price = price;
        this.duration = duration;
        this.location = location;
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEdition() {
        return edition;
    }

    public void setEdition(Integer edition) {
        this.edition = edition;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacher) {
        this.teacherId = teacher;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public Map<String, String> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<String, String> schedule) {
        this.schedule = schedule;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getNumberOfApplications() {
        return numberOfApplications;
    }

    public void setNumberOfApplications(Integer numberOfApplications) {
        this.numberOfApplications = numberOfApplications;
    }

    public Integer getMaxNumberOfApplications() {
        return maxNumberOfApplications;
    }

    public void setMaxNumberOfApplications(Integer maxNumberOfApplications) {
        this.maxNumberOfApplications = maxNumberOfApplications;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", edition=" + edition +
                ", teacher=" + teacherId +
                ", syllabus='" + syllabus + '\'' +
                ", program='" + program + '\'' +
                ", schedule=" + schedule +
                ", price=" + price +
                ", duration=" + duration +
                ", location='" + location + '\'' +
                ", numberOfApplications=" + numberOfApplications +
                ", maxNumberOfApplications=" + maxNumberOfApplications +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Objects.equals(name, course.name) && Objects.equals(edition, course.edition) && Objects.equals(teacherId, course.teacherId) && Objects.equals(syllabus, course.syllabus) && Objects.equals(program, course.program) && Objects.equals(schedule, course.schedule) && Objects.equals(price, course.price) && Objects.equals(duration, course.duration) && Objects.equals(location, course.location) && Objects.equals(numberOfApplications, course.numberOfApplications) && Objects.equals(maxNumberOfApplications, course.maxNumberOfApplications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, edition, teacherId, syllabus, program, schedule, price, duration, location, numberOfApplications, maxNumberOfApplications);
    }
}
