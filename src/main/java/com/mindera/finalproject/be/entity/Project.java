package com.mindera.finalproject.be.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;
import java.util.Objects;

@DynamoDbBean
public class Project {

    private String PK;
    private String SK;
    private List<String> students;
    private String courseId;
    private String name;
    private String gitHubRepo;
    private Integer grade;
    private Boolean active;

    public Project() {
    }

    public Project(List<String> students, String courseId, String name, String gitHubRepo) {
        this.students = students;
        this.courseId = courseId;
        this.name = name;
        this.gitHubRepo = gitHubRepo;
        this.active = true;
    }

    @DynamoDbPartitionKey
    public String getPK() {
        return PK;
    }

    public void setPK(String PK) {
        this.PK = PK;
    }

    @DynamoDbSortKey
    public String getSK() {
        return SK;
    }

    public void setSK(String SK) {
        this.SK = SK;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(PK, project.PK) && Objects.equals(SK, project.SK) && Objects.equals(students, project.students) && Objects.equals(courseId, project.courseId) && Objects.equals(name, project.name) && Objects.equals(gitHubRepo, project.gitHubRepo) && Objects.equals(grade, project.grade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(PK, SK, students, courseId, name, gitHubRepo, grade);
    }

    @Override
    public String toString() {
        return "Project{" +
                "PK='" + PK + '\'' +
                ", SK='" + SK + '\'' +
                ", students=" + students +
                ", courseId='" + courseId + '\'' +
                ", name='" + name + '\'' +
                ", gitHubRepo='" + gitHubRepo + '\'' +
                ", grade=" + grade +
                '}';
    }

}
