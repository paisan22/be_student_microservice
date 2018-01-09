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
        mailSender.send(createNewEmail(
                email,
                "Nieuw wachtwoord voor HSSTIP",
                "Je nieuwe wachtwoord voor HSSTIP is: " + password));
    }

    public String sendRegistrationMail(String email) {

        SimpleMailMessage newEmail = createNewEmail(
                email,
                "Nieuw account op HSSTIP aangemaakt",
                "Er is een nieuw account aangemaakt voor jou op hsstip. Je kunt inloggen met het volgende e-mail: " + email
        );

        mailSender.send(newEmail);

        return newEmail.getFrom();
    }

    public void sendErrorToOwner(String error, String datetime, String attemptMessage) {

        mailSender.send(createNewEmail(
                this.emailOwner,
                "Error notification from HSSTIP",
                "Date time: " + datetime + "\n" +
                        "Error: " + error + "\n" +
                        "Attempt to: " + attemptMessage + "\n"
        ));
    }

    public SimpleMailMessage createNewEmail(String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        return simpleMailMessage;
    }
}
