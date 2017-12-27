package nl.ipsenh.student.API;

import nl.ipsenh.student.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Created by paisanrietbroek on 05/12/2017.
 */
@RestController
@CrossOrigin(value = "*")
public class LoginAPI {

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/login")
    public HashMap login(@RequestHeader("email") String email, @RequestHeader("password") String password)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {

        return loginService.Authententicate(email, password);
    }

    @GetMapping(value = "/verify")
    public boolean verifyToken(@RequestHeader("token") String token) throws UnsupportedEncodingException {
        return loginService.verifyToken(token);
    }

    @PutMapping(value = "/reset_password")
    public HashMap resetPassword(@RequestBody HashMap<String, String> hashMap) throws NoSuchAlgorithmException {

        String email = hashMap.get("email");

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("status", "false");

        return loginService.resetPassword(email, stringStringHashMap);
    }

    @GetMapping(value = "/email_exists")
    public boolean emailExists(@RequestHeader HashMap<String, String> hashMap) {

        return loginService.emailExists(hashMap.get("email"));

    }
}
