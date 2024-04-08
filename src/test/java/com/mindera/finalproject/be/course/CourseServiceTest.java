package com.mindera.finalproject.be.course;

import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.entity.Person;
import com.mindera.finalproject.be.exception.student.PersonNotFoundException;
import com.mindera.finalproject.be.service.PersonService;
import com.mindera.finalproject.be.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mindera.finalproject.be.messages.Messages.COURSE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    private final String id = "COURSE#c75e7c3b-b422-4c41-a50b-c7fa8bce165c";
    @Mock
    private PersonService personService;
    @Mock
    private DynamoDbTable<Course> courseTable;
    @InjectMocks
    private CourseServiceImpl courseService;

    private Course getCourse() {
        Course course = new Course();
        course.setPK("COURSE#");
        course.setSK(id);
        course.setTeacherId("PERSON#ac271736-2fdc-4a5a-a32d-031bae808dfe");
        course.setName("TestName");
        course.setEdition(1);
        course.setSyllabus("TestSyllabus");
        course.setProgram("TestProgram");
        course.setSchedule("TestSchedule");
        course.setPrice(new BigDecimal("100"));
        course.setDuration(1);
        course.setLocation("LOCATION");
        course.setActive(true);
        course.setNumberOfApplications(0);
        course.setMaxNumberOfApplications(10);
        return course;
    }

    private PersonPublicDto getPersonPublicDto() {
        return new PersonPublicDto(
                "PERSON#ac271736-2fdc-4a5a-a32d-031bae808dfe",
                "TestEmail",
                "TestFN",
                "TestLN",
                "TestRole",
                "TestUS",
                LocalDate.now(),
                "TestAddress"
        );
    }

    private Person getPerson() {
        Person person = new Person();
        person.setPK("PERSON#");
        person.setSK("PERSON#ac271736-2fdc-4a5a-a32d-031bae808dfe");
        person.setEmail("TestEmail");
        person.setFirstName("TestFN");
        person.setLastName("TestLN");
        person.setRole("TestRole");
        person.setUsername("TestUS");
        person.setDateOfBirth(LocalDate.now());
        person.setAddress("TestAddress");
        person.setActive(true);
        return person;
    }

    @Test
    void testGetById() throws Exception {
        Course course = getCourse();

        when(courseTable.getItem(any(Key.class))).thenReturn(course);
        when(personService.getById(anyString())).thenReturn(getPersonPublicDto());
        when(personService.findById(anyString())).thenReturn(getPerson());

        CoursePublicDto result = courseService.getById(id);

        verify(courseTable, times(1)).getItem(any(Key.class));
        verify(personService, times(1)).getById(anyString());

        assertNotNull(result);
        assertEquals(course.getSK(), result.id());
    }

    @Test
    void testGetByIdCourseNotFound() {
        when(courseTable.getItem(any(Key.class))).thenReturn(null);

        try {
            courseService.getById(id);
        } catch (Exception e) {
            assertEquals(COURSE_NOT_FOUND + id, e.getMessage());
        }
    }

    @Test
    void testGetAll() throws PersonNotFoundException {
        List<Course> course = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            course.add(getCourse());
        }
        Page<Course> page1 = Page.create(course.subList(0, 100));
        Page<Course> page2 = Page.create(course.subList(100, 101));

        SdkIterable<Page<Course>> courses = () -> List.of(page1, page2).iterator();

        PageIterable<Course> pageIterable = PageIterable.create(courses);

        when(courseTable.query(any(QueryEnhancedRequest.class))).thenReturn(pageIterable);
        when(personService.findById(anyString())).thenReturn(getPerson());

        List<CoursePublicDto> result = courseService.getAll(0, 100);
        List<CoursePublicDto> result2 = courseService.getAll(1, 100);

        verify(courseTable, times(2)).query(any(QueryEnhancedRequest.class));
        assertEquals(100, result.size());
        assertEquals(1, result2.size());
    }

    @Test
    void testGetAllTeacherNotFound() throws PersonNotFoundException {
        List<Course> course = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            course.add(getCourse());
        }
        Page<Course> page1 = Page.create(course.subList(0, 100));

        SdkIterable<Page<Course>> courses = () -> List.of(page1).iterator();

        PageIterable<Course> pageIterable = PageIterable.create(courses);

        when(courseTable.query(any(QueryEnhancedRequest.class))).thenReturn(pageIterable);
        when(personService.findById(anyString())).thenThrow(new PersonNotFoundException("error"));

        List<CoursePublicDto> result = courseService.getAll(0, 100);

        verify(courseTable, times(1)).query(any(QueryEnhancedRequest.class));
        verify(personService, times(100)).findById(anyString());
        assertEquals(100, result.size());
        for (int i = 0; i < 100; i++) {
            assertNull(result.get(i).teacher());
        }
    }
}
