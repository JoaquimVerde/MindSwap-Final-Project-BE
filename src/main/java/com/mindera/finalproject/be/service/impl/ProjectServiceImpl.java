package com.mindera.finalproject.be.service.impl;

import com.mindera.finalproject.be.converter.ProjectConverter;
import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.dto.project.ProjectCreateDto;
import com.mindera.finalproject.be.dto.project.ProjectPublicDto;
import com.mindera.finalproject.be.dto.project.ProjectUpdateGradeDto;
import com.mindera.finalproject.be.entity.Project;
import com.mindera.finalproject.be.exception.course.CourseNotFoundException;
import com.mindera.finalproject.be.exception.project.ProjectNotFoundException;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.CourseService;
import com.mindera.finalproject.be.service.PersonService;
import com.mindera.finalproject.be.service.ProjectService;
import com.mindera.finalproject.be.testEmail.Email;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ProjectServiceImpl implements ProjectService {

    private final String TABLE_PROJECT = "Project";
    private final String PROJECT = "PROJECT#";
    private DynamoDbTable<Project> projectTable;
    @Inject
    private CourseService courseService;
    @Inject
    private PersonService personService;

    @Inject
    Email email;


    @Inject
    void projectEnhancedService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        projectTable = dynamoEnhancedClient.table(TABLE_PROJECT, TableSchema.fromBean(Project.class));

    }

    @Override
    public List<ProjectPublicDto> getAll() {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(s -> s.partitionValue(PROJECT).sortValue(PROJECT));
        SdkIterable<Page<Project>> projects = projectTable.query(queryConditional);
        List<Project> projectsList = new ArrayList<>();
        projects.forEach(page -> projectsList.addAll(page.items()));
        email.sendEmail("fadg100@gmail.com");
        return projectsList.stream().filter(Project::getActive).map(this::mapProjectList).toList();

    }

    private List<Project> getAllProjects() {
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(s -> s.partitionValue(PROJECT).sortValue(PROJECT));
        SdkIterable<Page<Project>> projects = projectTable.query(queryConditional);
        List<Project> projectsList = new ArrayList<>();
        projects.forEach(page -> projectsList.addAll(page.items()));
        return projectsList;
    }

    @Override
    public ProjectPublicDto getById(String id) throws ProjectNotFoundException, PersonNotFoundException, CourseNotFoundException {
        Project project = verifyIfProjectExists(id);
        CoursePublicDto course = courseService.getById(project.getCourseId());
        List<PersonPublicDto> students = new ArrayList<>();
        for (String studentId : project.getStudents()) {
            students.add(personService.getById(studentId));
        }
        return ProjectConverter.fromEntityToPublicDto(project, course, students);
    }

    @Override
    public ProjectPublicDto create(ProjectCreateDto projectCreateDto) throws ProjectNotFoundException, PersonNotFoundException, CourseNotFoundException {
        Project project = ProjectConverter.convertFromDtoToEntity(projectCreateDto);
        project.setPK(PROJECT);
        project.setSK(PROJECT + UUID.randomUUID());
        projectTable.putItem(project);
        CoursePublicDto coursePublicDto = courseService.getById(projectCreateDto.courseId());
        List<PersonPublicDto> students = new ArrayList<>();
        for (String studentId : projectCreateDto.studentIds()) {
            students.add(personService.getById(studentId));
        }
        return ProjectConverter.fromEntityToPublicDto(project, coursePublicDto, students);
    }

    @Override
    public ProjectPublicDto update(String id, ProjectCreateDto projectCreateDto) throws ProjectNotFoundException, PersonNotFoundException, CourseNotFoundException {
        Project project = verifyIfProjectExists(id);
        if (!projectCreateDto.name().equals(project.getName())) {
            project.setName(projectCreateDto.name());
        }
        if (!projectCreateDto.studentIds().equals(project.getStudents())) {
            project.setStudents(projectCreateDto.studentIds());
        }
        if (!projectCreateDto.courseId().equals(project.getCourseId())) {
            project.setCourseId(projectCreateDto.courseId());
        }
        if (!projectCreateDto.gitHubRepo().equals(project.getGitHubRepo())) {
            project.setGitHubRepo(projectCreateDto.gitHubRepo());
        }
        projectTable.putItem(project);
        CoursePublicDto course = courseService.getById(project.getCourseId());
        List<PersonPublicDto> students = new ArrayList<>();
        for (String studentId : project.getStudents()) {
            students.add(personService.getById(studentId));
        }
        return ProjectConverter.fromEntityToPublicDto(project, course, students);
    }

    @Override
    public ProjectPublicDto updateGrade(String id, ProjectUpdateGradeDto projectUpdateGradeDto) throws ProjectNotFoundException, PersonNotFoundException, CourseNotFoundException {
        Project project = verifyIfProjectExists(id);
        project.setGrade(projectUpdateGradeDto.grade());
        CoursePublicDto course = courseService.getById(project.getCourseId());
        List<PersonPublicDto> students = new ArrayList<>();
        for (String studentId : project.getStudents()) {
            students.add(personService.getById(studentId));
        }
        projectTable.updateItem(project);
        return ProjectConverter.fromEntityToPublicDto(project, course, students);
    }

    @Override
    public void delete(String id) throws ProjectNotFoundException {
        Project project = verifyIfProjectExists(id);
        project.setActive(false);
        projectTable.updateItem(project);
    }

    @Override
    public List<ProjectPublicDto> getProjectsByPersonId(String personId) throws PersonNotFoundException {
        personService.findById(personId);
        List<Project> projectsOfPerson = getAllProjects().stream().filter(project -> project.getStudents().contains(personId)).toList();
        return projectsOfPerson.stream().filter(Project::getActive).map(this::mapProjectList).toList();
    }

    @Override
    public List<ProjectPublicDto> getProjectsByCourseId(String courseId) throws CourseNotFoundException {
        courseService.findById(courseId);
        QueryConditional queryConditional = QueryConditional.sortBeginsWith(s -> s.partitionValue(courseId).sortValue(PROJECT));
        DynamoDbIndex<Project> projectIndex = projectTable.index("GSIPK1");
        SdkIterable<Page<Project>> projects = projectIndex.query(queryConditional);
        List<Project> projectsList = new ArrayList<>();
        projects.forEach(page -> projectsList.addAll(page.items()));
        return projectsList.stream().filter(Project::getActive).map(this::mapProjectList).toList();
    }

    private ProjectPublicDto mapProjectList(Project project) {
        CoursePublicDto course = null;
        List<PersonPublicDto> students = new ArrayList<>();
        try {
            course = courseService.getById(project.getCourseId());
            for (String studentId : project.getStudents()) {
                students.add(personService.getById(studentId));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ProjectConverter.fromEntityToPublicDto(project, course, students);
    }

    private Project verifyIfProjectExists(String id) throws ProjectNotFoundException {
        Project project = projectTable.getItem(Key.builder().partitionValue(PROJECT).sortValue(id).build());
        if (project == null) {
            throw new ProjectNotFoundException("Project with id " + id + " not found");
        }
        return project;
    }
}
