package com.mindera.finalproject.be.course;

import com.mindera.finalproject.be.dto.course.CoursePublicDto;
import com.mindera.finalproject.be.dto.person.PersonPublicDto;
import com.mindera.finalproject.be.entity.Course;
import com.mindera.finalproject.be.service.PersonService;
import com.mindera.finalproject.be.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        course.setSK("COURSE#c75e7c3b-b422-4c41-a50b-c7fa8bce165c");
        course.setTeacherId("PERSON#ac271736-2fdc-4a5a-a32d-031bae808dfe");
        course.setName("TestName");
        course.setEdition(1);
        course.setSyllabus("TestSyllabus");
        course.setProgram("TestProgram");
        course.setSchedule("TestSchedule");
        course.setPrice(new BigDecimal("100"));
        course.setDuration(1);
        course.setLocation("TestLocation");
        course.setActive(true);
        course.setNumberOfApplications(0);
        course.setMaxNumberOfApplications(10);
        return course;
    }

    private PersonPublicDto getCoursePublicDto() {
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

    @Test
    void testGetById() throws Exception {
        Course course = getCourse();

        when(courseTable.getItem(any(Key.class))).thenReturn(course);
        when(personService.getById(anyString())).thenReturn(getCoursePublicDto());

        CoursePublicDto result = courseService.getById(id);

        verify(courseTable, times(1)).getItem(any(Key.class));
        verify(personService, times(1)).getById(anyString());

        assertNotNull(result);
    }
}
