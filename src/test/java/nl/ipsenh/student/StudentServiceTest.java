package nl.ipsenh.student;

import nl.ipsenh.student.model.Student;
import nl.ipsenh.student.repository.StudentRepository;
import nl.ipsenh.student.service.StudentService;
import nl.ipsenh.student.service.email.EmailService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static org.hamcrest.core.Is.is;

/**
 * Created by paisanrietbroek on 18/12/2017.
 */

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testCreateStudentHashmap() {

        Student student = Student.builder()
                .email("test@email")
                .build();

        HashMap<String, String> result = this.studentService.createStudentHashmap(student);

        Assert.assertThat(result.get("message"), is("false"));
        Assert.assertThat(result.get("email"), is("test@email"));

    }

    @Test
    public void testHashPassword() throws NoSuchAlgorithmException {
        String password = "password123";
        String hash = "482C811DA5D5B4BC6D497FFA98491E38";

        String result = studentService.hashPassword(password);

        Assert.assertThat(result, is(hash));

    }

}
