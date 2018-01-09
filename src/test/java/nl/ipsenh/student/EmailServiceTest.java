package nl.ipsenh.student;

import nl.ipsenh.student.service.email.EmailService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;

/**
 * Created by paisanrietbroek on 09/01/2018.
 */

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testCreateNewEmail() {

        String email = "hans@mail.nl";
        String subject = "the subject";
        String text = "this is the content of the email";

        SimpleMailMessage result = this.emailService.createNewEmail(email, subject, text);

        Assert.assertThat(result, isA(SimpleMailMessage.class));
        Assert.assertThat(Arrays.toString(result.getTo()), is("[hans@mail.nl]"));
        Assert.assertThat(result.getSubject(), is("the subject"));
        Assert.assertThat(result.getText(), is("this is the content of the email"));

    }

}
