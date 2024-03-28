package com.mindera.finalproject.be.repository.schema;

import com.mindera.finalproject.be.entity.Teacher;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.entity.Registration;
import com.mindera.finalproject.be.entity.Student;
import jakarta.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Singleton
public class TableSchemas {


//    Insert TableSchemas here
      public static final TableSchema<Teacher> teacherTableSchema =
            TableSchema.builder(Teacher.class)
                    .newItemSupplier(Teacher::new)
                    .addAttribute(Long.class, a -> a.name("id")
                            .getter(Teacher::getId)
                            .setter(Teacher::setId)
                            .tags(StaticAttributeTags.primaryPartitionKey()))
                    .addAttribute(String.class, a -> a.name("email")
                            .getter(Teacher::getEmail)
                            .setter(Teacher::setEmail)
                            .tags(StaticAttributeTags.primarySortKey()))
                    .addAttribute(String.class, a -> a.name("firstName")
                            .getter(Teacher::getFirstName)
                            .setter(Teacher::setFirstName))
                    .addAttribute(String.class, a -> a.name("lastName")
                            .getter(Teacher::getLastName)
                            .setter(Teacher::setLastName))
                    .addAttribute(String.class, a -> a.name("role")
                            .getter(Teacher::getRole)
                            .setter(Teacher::setRole))
                    .addAttribute(String.class, a -> a.name("username")
                            .getter(Teacher::getUsername)
                            .setter(Teacher::setUsername))
                    .addAttribute(LocalDate.class, a -> a.name("dateOfBirth")
                            .getter(Teacher::getDateOfBirth)
                            .setter(Teacher::setDateOfBirth))
                    .addAttribute(Integer.class, a -> a.name("age")
                            .getter(Teacher::getAge))
                    .addAttribute(String.class, a -> a.name("address")
                            .getter(Teacher::getAddress)
                            .setter(Teacher::setAddress))

    public static final TableSchema<Registration> registrationTableSchema =
            TableSchema.builder(Registration.class)
            .newItemSupplier(Registration::new)
            .addAttribute(String.class, a -> a.name("registrationId")
                    .getter(Registration::getRegistrationId)
                    .setter(Registration::setRegistrationId)
                    .tags(StaticAttributeTags.primaryPartitionKey()))
                .addAttribute(String.class, a -> a.name("compositeKey")
                        .getter(Registration::getCompositeKey)
                        .setter(Registration::setCompositeKey)
                        .tags(StaticAttributeTags.primarySortKey()))
                .addAttribute(Long.class, a -> a.name("personId")
                        .getter(Registration::getPersonId)
                        .setter(Registration::setPersonId))
                .addAttribute(Long.class, a -> a.name("courseId")
                        .getter(Registration::getCourseId)
                        .setter(Registration::setCourseId))
                .addAttribute(String.class, a -> a.name("status")
                        .getter(Registration::getStatus)
                        .setter(Registration::setStatus))
                .addAttribute(String.class, a -> a.name("finalGrade")
                        .getter(Registration::getFinalGrade)
                        .setter(Registration::setFinalGrade))
                .addAttribute(Boolean.class, a -> a.name("active")
                        .getter(Registration::getActive)
                        .setter(Registration::setActive))
                .build();


    public static final TableSchema<Student> studentTableSchema =
            TableSchema.builder(Student.class)
                    .newItemSupplier(Student::new)
                    .addAttribute(Long.class, a -> a.name("id")
                            .getter(Student::getId)
                            .setter(Student::setId)
                            .tags(StaticAttributeTags.primaryPartitionKey()))
                    .addAttribute(String.class, a -> a.name("email")
                            .getter(Student::getEmail)
                            .setter(Student::setEmail)
                            .tags(StaticAttributeTags.primarySortKey()))
                    .addAttribute(String.class, a -> a.name("first name")
                            .getter(Student::getFirstName)
                            .setter(Student::setFirstName))
                    .addAttribute(String.class, a -> a.name("last name")
                            .getter(Student::getLastName)
                            .setter(Student::setLastName))
                    .addAttribute(String.class, a -> a.name("role")
                            .getter(Student::getRole)
                            .setter(Student::setRole))
                    .addAttribute(String.class, a -> a.name("username")
                            .getter(Student::getUsername)
                            .setter(Student::setUsername))
                    .addAttribute(LocalDate.class, a -> a.name("date of birth")
                            .getter(Student::getDateOfBirth)
                            .setter(Student::setDateOfBirth))
                    .addAttribute(Integer.class, a -> a.name("age")
                            .getter(Student::getAge))
                    .addAttribute(String.class, a -> a.name("address")
                            .getter(Student::getAddress)
                            .setter(Student::setAddress))
                    .addAttribute(String.class, a -> a.name("curriculum")
                            .getter(Student::getCurriculum)
                            .setter(Student::setCurriculum))
                    .build();

    public static final TableSchema<Course> courseTableSchema =
            TableSchema.builder(Course.class)
                    .newItemSupplier(Course::new)
                    .addAttribute(Long.class, a -> a.name("id")
                            .getter(Course::getId)
                            .setter(Course::setId)
                            .tags(StaticAttributeTags.primaryPartitionKey()))
                    .addAttribute(String.class, a -> a.name("name")
                            .getter(Course::getName)
                            .setter(Course::setName))
                    .addAttribute(Integer.class, a -> a.name("edition")
                            .getter(Course::getEdition)
                            .setter(Course::setEdition))
                    .addAttribute(Person.class, a -> a.name("teacher")
                            .getter(Course::getTeacher)
                            .setter(Course::setTeacher)
                            .tags(StaticAttributeTags.primarySortKey()))
                    .addAttribute(String.class, a -> a.name("syllabus")
                            .getter(Course::getSyllabus)
                            .setter(Course::setSyllabus))
                    .addAttribute(String.class, a -> a.name("program")
                            .getter(Course::getProgram)
                            .setter(Course::setProgram))
                    .addAttribute(Map.class, a -> a.name("schedule")
                            .getter(Course::getSchedule)
                            .setter(Course::setSchedule))
                    .addAttribute(BigDecimal.class, a -> a.name("price")
                            .getter(Course::getPrice)
                            .setter(Course::setPrice))
                    .addAttribute(Integer.class, a -> a.name("duration")
                            .getter(Course::getDuration)
                            .setter(Course::setDuration))
                    .addAttribute(String.class, a -> a.name("location")
                            .getter(Course::getLocation)
                            .setter(Course::setLocation))
                    .addAttribute(Integer.class, a -> a.name("numberOfApplications")
                            .getter(Course::getNumberOfApplications)
                            .setter(Course::setNumberOfApplications))
                    .addAttribute(Integer.class, a -> a.name("maxNumberOfApplications")
                            .getter(Course::getMaxNumberOfApplications)
                            .setter(Course::setMaxNumberOfApplications))
                    .build();
}
