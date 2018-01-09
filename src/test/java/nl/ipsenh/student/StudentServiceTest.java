package nl.ipsenh.student;

import nl.ipsenh.student.model.Student;
import nl.ipsenh.student.repository.StudentRepository;
import nl.ipsenh.student.service.StudentService;
import nl.ipsenh.student.service.email.EmailService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

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

    private Student student;

    @Before
    public void setup() {
        this.student = Student.builder()
                .surName("hans")
                .middleName("de")
                .lastName("gans")
                .email("test@email")
                .password("password123")
                .build();

        when(studentRepository.findByEmail(any(String.class)))
                .thenReturn(this.student);

    }

    @Test
    public void testCreateStudentHashmap() {

        HashMap<String, String> result = this.studentService.createStudentHashmap(this.student);

        Assert.assertThat(result.get("message"), is("false"));
        Assert.assertThat(result.get("email"), is("test@email"));

    }

    @Test
    public void testHashPassword() throws NoSuchAlgorithmException {
        String hash = "482C811DA5D5B4BC6D497FFA98491E38";

        String result = studentService.hashPassword(this.student.getPassword());

        Assert.assertThat(result, is(hash));

    }

    @Test
    public void testCreateStudent() throws NoSuchAlgorithmException {
        HashMap<String, String> student = this.studentService.createStudent(this.student);

        Assert.assertThat(student.get("message"), is("true"));
        Assert.assertThat(student.get("email"), is("test@email"));

        Assert.assertThat(student.get("message"), is("true"));
    }

    @Test
    public void testGetRandomPassword() {
        String randomPassword1 = this.studentService.getRandomPassword();
        String randomPassword2 = this.studentService.getRandomPassword();

        Assert.assertThat(randomPassword1, is(not(randomPassword2)));
    }

    @Test
    public void testResetPassword() throws NoSuchAlgorithmException {

        boolean result = this.studentService.resetPassword("test@email.nl");

        Assert.assertThat(result, is(true));

        Mockito.verify(this.studentRepository, times(1)).findByEmail(Matchers.contains("test@email.nl"));
        Mockito.verify(this.emailService, times(1)).sendNewPassword(Matchers.contains("test@email.nl"), any(String.class));
        Mockito.verify(this.studentRepository, times(1)).save(this.student);

    }

}
