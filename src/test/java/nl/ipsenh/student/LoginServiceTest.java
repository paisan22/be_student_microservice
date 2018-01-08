package nl.ipsenh.student;

import nl.ipsenh.student.API.DocentAPI;
import nl.ipsenh.student.model.Student;
import nl.ipsenh.student.repository.StudentRepository;
import nl.ipsenh.student.service.LoginService;
import nl.ipsenh.student.service.StudentService;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by paisanrietbroek on 05/12/2017.
 */

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class LoginServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DocentAPI docentAPI;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private LoginService loginService;

    private Student student;

    @Before
    public void setup() throws NoSuchAlgorithmException {
        student = Student.builder()
                .surName("testTokenUser")
                .email("test@mail.com")
                .password("password123") // hash value of password123
                .build();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "1");
        jsonObject.put("name", "Docent");

        when(this.docentAPI.getDocentByEmail(any(HashMap.class)))
                .thenReturn(jsonObject);

        when(this.studentService.getStudentByEmail("test@mail.com"))
                .thenReturn(this.student);
        when(this.studentService.hashPassword(any(String.class)))
                .thenReturn("password123");
    }

    @Test
    public void testAuthenticate() throws UnsupportedEncodingException, NoSuchAlgorithmException {

        Map<String, String> result = this.loginService.authententicate("test@mail.com", "password123");

        Assert.assertThat(result.get("email"), is("true"));
        Assert.assertThat(result.get("password"), is("true"));
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
