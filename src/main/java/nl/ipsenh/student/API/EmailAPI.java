package nl.ipsenh.student.API;

import nl.ipsenh.student.service.email.EmailService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Created by paisanrietbroek on 11/12/2017.
 */

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "/email")
public class EmailAPI {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send_registration_email")
    public String sendEmail(@RequestBody HashMap<String, String> hashMap) {
        String email = hashMap.get("email");
        String password = hashMap.get("password");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("password", password);

        return emailService.sendRegistrationMail(email, password);
    }

    @PostMapping("/email_error")
    public void emailErrorToOwner(@RequestBody HashMap<String, String> hashMap) {

        String error = hashMap.get("error");
        String datetime = hashMap.get("datetime");
        String attemptMessage = hashMap.get("attemptMessage");

        this.emailService.sendErrorToOwner(error, datetime, attemptMessage);

    }

}
