package com.mindera.finalproject.be.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class Projects {

    private Long id;

    private Long studentId;

    private Long courseId;

    private String name;

    private String gitHubRepo;

    private int grade;

    public Projects(Long id, Long studentId, Long courseId, String name, String gitHubRepo, int grade) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.name = name;
        this.gitHubRepo = gitHubRepo;
        this.grade = grade;
    }

    @DynamoDbSortKey
    public Long getId() {
        return id;
    }


    @DynamoDbPartitionKey
    public Long getStudentId(){
        return studentId;
    }

    @DynamoDbSortKey
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
}
