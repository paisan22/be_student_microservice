package nl.ipsenh.student;

import nl.ipsenh.student.repository.StudentRepository;
import nl.ipsenh.student.service.StudentService;
import nl.ipsenh.student.service.email.EmailService;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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



}
