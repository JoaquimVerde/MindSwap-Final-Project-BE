package com.mindera.finalproject.be.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@DynamoDbBean
public class Project {

    private String id;
    private List<Person> students;
    private Long courseId;
    private String name;
    private String gitHubRepo;
    private int grade;


    public Project() {
    }

    public Project(String id, List<Person> students, Long courseId, String name, String gitHubRepo, int grade) {
        this.id = id;
        this.students = students;
        this.courseId = courseId;
        this.name = name;
        this.gitHubRepo = gitHubRepo;
        this.grade = grade;
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public List<Person> getStudentIds(){
        return students;
    }

    public Long getCourseId(){
        return courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGitHubRepo() {
        return gitHubRepo;
    }

    public void setGitHubRepo(String gitHubRepo) {
        this.gitHubRepo = gitHubRepo;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setStudents(List<Person> students) {
        this.students = students;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setId(String id) {
        this.id = id;
    }
}
