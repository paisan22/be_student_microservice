package nl.ipsenh.student;


import nl.ipsenh.student.API.StudentAPI;
import nl.ipsenh.student.model.Student;
import nl.ipsenh.student.service.StudentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Created by paisanrietbroek on 28/11/2017.
 */

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class StudentAPITest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentAPI studentAPI;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(studentAPI).build();

        // stubbing
        Student s1 = Student.builder()
                .sureName("testsurename1")
                .lastName("testlastname1")
                .build();
        Student s2 = Student.builder()
                .sureName("testsurename2")
                .lastName("testlastname2")
                .build();

        ArrayList<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);

        when(studentService.getAllStudents()).thenReturn(students);

    }

    @Test
    public void getAllStudentsTest() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(get("/students/all"));

        verify(studentService ,times(1)).getAllStudents();

    }

}
