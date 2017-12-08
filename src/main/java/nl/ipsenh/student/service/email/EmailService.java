package nl.ipsenh.student.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Created by paisanrietbroek on 08/12/2017.
 */

@Service
public class EmailService {

    @Autowired
    public JavaMailSender mailSender;

    public void sendNewPassword(String email, String password) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Nieuw wachtwoord voor HSSTIP");
        simpleMailMessage.setText("Je nieuwe wachtwoord voor HSSTIP is: " + password);
        mailSender.send(simpleMailMessage);
    }
}
