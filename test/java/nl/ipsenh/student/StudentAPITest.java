package nl.ipsenh.student;


import nl.ipsenh.student.API.StudentAPI;
import nl.ipsenh.student.model.Student;
import nl.ipsenh.student.service.StudentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        Student student = Student.builder()
                .sureName("Hans")
                .middleName("de")
                .lastName("Gans")
                .email("s123@student.hsleiden.nl")
                .number("s123")
                .phoneNumber("0612341234")
                .password("password123")
                .build();

        ArrayList<Student> students = new ArrayList<>();
        students.add(student);

        when(studentService.getAllStudents()).thenReturn(students);

    }

    @Test
    public void getAllStudentsTest() throws Exception {
        this.mockMvc.perform(get("/students/all"));
        verify(studentService ,times(1)).getAllStudents();

    }

    @Test
    public void getStudentByNumberTest() throws Exception {
        this.mockMvc.perform(get("/students/s1234"))
                .andExpect(status().isOk());

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(studentService ,times(1)).getStudentByNumber(stringArgumentCaptor.capture());
    }

    @Test
    public void createNewStudent() throws Exception {
        this.mockMvc.perform(post("/students"))
                .andExpect(status().isOk());

        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentService, times(1)).createStudent(studentArgumentCaptor.capture());
    }

}
