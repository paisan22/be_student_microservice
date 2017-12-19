package nl.ipsenh.student;


import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(studentAPI).build();

        // stubbing
        Student student = Student.builder()
                .surName("Hans")
                .middleName("de")
                .lastName("Gans")
                .email("s123@student.hsleiden.nl")
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
    public void createNewStudent() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        Student student = Student.builder()
                .surName("test")
                .surName("test")
                .build();

        String json = objectMapper.writeValueAsString(student);

        this.mockMvc.perform(post("/students")
        .content(json)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentService, times(1)).createStudent(studentArgumentCaptor.capture());
    }

    @Test
    public void updateStudentTest() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        Student student = Student.builder()
                .surName("test")
                .surName("test")
                .build();

        String json = objectMapper.writeValueAsString(student);

        this.mockMvc.perform(put("/students")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentService, times(1)).updateStudent(studentArgumentCaptor.capture());
    }

    @Test
    public void deleteStudentTest() throws Exception {

        this.mockMvc.perform(delete("/students/1"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent("1");
    }

}
