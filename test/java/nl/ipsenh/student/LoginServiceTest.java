package nl.ipsenh.student;

import nl.ipsenh.student.model.Student;
import nl.ipsenh.student.repository.StudentRepository;
import nl.ipsenh.student.service.LoginService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static org.hamcrest.core.Is.is;

/**
 * Created by paisanrietbroek on 05/12/2017.
 */

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class LoginServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private LoginService loginService;

    private Student student;

    @Before
    public void setup() {
        student = Student.builder()
                .surName("testTokenUser")
                .email("test@mail.com")
                .build();
    }

    @Test
    public void testSetLoginStatusHashmap() {

        HashMap<String, String> result = this.loginService.setLoginStatusHashmap();

        Assert.assertThat(result.get("email"), is("false"));
        Assert.assertThat(result.get("password"), is("false"));

    }

    @Test
    public void testStudentExists() {

        boolean result = loginService.studentExists(this.student);
        Assert.assertTrue(result);

        Student one = this.studentRepository.findOne("0");
        boolean result2 = loginService.studentExists(one);

        Assert.assertFalse(result2);


    }

    @Test
    public void createTokenTest() throws UnsupportedEncodingException {
        String token = loginService.createToken(this.student);

        boolean result = token instanceof String;

        Assert.assertTrue(result);
    }

    @Test
    public void verifyTokenTest() throws UnsupportedEncodingException {

        String token = loginService.createToken(this.student);
        boolean result = loginService.verifyToken(token);

        Assert.assertTrue(result);

        boolean result2 = loginService.verifyToken("This.iS.A.Fake.TokEn");

        Assert.assertFalse(result2);
    }

    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }


}
