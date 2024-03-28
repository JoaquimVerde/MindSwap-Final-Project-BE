package com.mindera.finalproject.be.repository.schema;

import com.mindera.finalproject.be.entity.Project;
import jakarta.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;

import java.net.URL;
import java.util.List;


@Singleton
public class TableSchemas {
//    Insert TableSchemas here

    public static final TableSchema<Project> ProjectTableSchema =
            TableSchema.builder(Project.class)
                    .newItemSupplier(Project::new)
                    .addAttribute(Long.class, a -> a.name("id")
                            .getter(Project::getId)
                            .setter(Project::setId)
                            .tags(StaticAttributeTags.primaryPartitionKey()))
                    .addAttribute(List.class, a -> a.name("studentIds")
                            .getter(Project::getStudentIds)
                            .setter(Project::setStudentIds)
                            .tags(StaticAttributeTags.primarySortKey()))
                    .addAttribute(Long.class, a -> a.name("courseId")
                            .getter(Project::getCourseId)
                            .setter(Project::setCourseId)
                            .tags(StaticAttributeTags.primarySortKey()))
                    .addAttribute(String.class, a -> a.name("name")
                            .getter(Project::getName)
                            .setter(Project::setName))
                    .addAttribute(URL.class, a -> a.name("gitHubRepo")
                            .getter(Project::getGitHubRepo)
                            .setter(Project::setGitHubRepo))
                    .addAttribute(int.class, a -> a.name("grade")
                            .getter(Project::getGrade)
                            .setter(Project::setGrade))
                    .build();
}
