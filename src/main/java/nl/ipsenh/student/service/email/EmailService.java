package nl.ipsenh.student.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Created by paisanrietbroek on 08/12/2017.
 */

@Component
public class EmailService {

    @Autowired
    public JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailOwner;

    public void sendNewPassword(String email, String password) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Nieuw wachtwoord voor HSSTIP");
        simpleMailMessage.setText("Je nieuwe wachtwoord voor HSSTIP is: " + password);
        mailSender.send(simpleMailMessage);
    }

    public String sendRegistrationMail(String email) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Nieuw account op HSSTIP aangemaakt");
        simpleMailMessage.setText("Er is een nieuw account aangemaakt voor jou op hsstip. Je kunt inloggen met het volgende e-mail: " + email);

        mailSender.send(simpleMailMessage);

        return simpleMailMessage.getFrom();
    }

    public void sendErrorToOwner(String error, String datetime, String attemptMessage) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(this.emailOwner);
        simpleMailMessage.setSubject("Error notification from HSSTIP");
        simpleMailMessage.setText(
                "Date time: " + datetime + "\n" +
                "Error: " + error + "\n" +
                "Attempt to: " + attemptMessage + "\n"
        );

        mailSender.send(simpleMailMessage);
    }

}
