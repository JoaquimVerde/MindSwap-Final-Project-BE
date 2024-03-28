package com.mindera.finalproject.be.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.net.URL;
import java.util.List;

@DynamoDbBean
public class Project {

    private Long id;
    private List<Long> studentIds;
    private Long courseId;
    private String name;
    private String gitHubRepo;
    private int grade;


    public Project() {
    }

    public Project(Long id, List<Long> studentIds, Long courseId, String name, String gitHubRepo, int grade) {
        this.id = id;
        this.studentIds = studentIds;
        this.courseId = courseId;
        this.name = name;
        this.gitHubRepo = gitHubRepo;
        this.grade = grade;
    }

    @DynamoDbPartitionKey
    public Long getId() {
        return id;
    }

    @DynamoDbSortKey
    public List<Long> getStudentIds(){
        return studentIds;
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

    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
