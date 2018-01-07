package nl.ipsenh.student.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import nl.ipsenh.student.API.DocentAPI;
import nl.ipsenh.student.model.Student;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by paisanrietbroek on 04/12/2017.
 */

@Service
public class LoginService {

    @Autowired
    private StudentService studentService;

    @Autowired
    private DocentAPI docentAPI;

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String FALSE = "false";

    public HashMap<String, String> Authententicate(String email, String password)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {

        HashMap<String, String> loginStatus = setLoginStatusHashmap();

        // check if email exist
        Student studentByEmail = studentService.getStudentByEmail(email);

        if (studentExists(studentByEmail)) {
            loginStatus.put(EMAIL, "true");
        }

        loginStatus.put(PASSWORD, isCorrectPassword(studentByEmail, password));

        if (isCorrectLogin(loginStatus)) {
            loginStatus.put("token", createToken(studentByEmail));
        }

        return loginStatus;
    }

    public HashMap<String, String> setLoginStatusHashmap() {
        HashMap<String, String> loginStatus = new HashMap<>();
        loginStatus.put(EMAIL, FALSE);
        loginStatus.put(PASSWORD, FALSE);

        return loginStatus;
    }

    public boolean studentExists(Student student) {
        return (student != null) && (student instanceof Student);
    }

    public String isCorrectPassword(Student student, String password) throws NoSuchAlgorithmException {
        try {
            return String.valueOf(Objects.equals(student.getPassword(), studentService.hashPassword(password)));
        } catch (NullPointerException e) {
            return FALSE;
        }

    }

    public boolean isCorrectLogin(HashMap<String, String> hashmap) {
        String email = hashmap.get(EMAIL);
        String password = hashmap.get(PASSWORD);

        return (email == "true" && password == "true");
    }

    public String createToken(Student student) throws JWTCreationException, UnsupportedEncodingException {

        Algorithm algorithm = Algorithm.HMAC256("secret");

        HashMap<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("student", student.getEmail());

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("slb_email", student.getSlbEmail());

        JSONObject docentByEmail = docentAPI.getDocentByEmail(stringStringHashMap);

        return JWT.create()
                .withClaim("slb_id", docentByEmail.get("id").toString())
                .withClaim("slb_name", docentByEmail.get("name").toString())
                .withClaim(EMAIL, student.getEmail())
                .withClaim("surname", student.getSurName())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000)) // plus 30 minutes from now
                .withIssuer("auth0")
                .sign(algorithm);
    }

    public boolean verifyToken(String token) throws UnsupportedEncodingException {

        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");

            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();

            jwtVerifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public HashMap<String, String> resetPassword(String email, HashMap<String, String> stringStringHashMap) throws NoSuchAlgorithmException {

        if (emailExists(email)) {
            if (studentService.resetPassword(email)) {
                stringStringHashMap.put("status", "true");
                stringStringHashMap.put("message", "reset password done");
                return stringStringHashMap;
            }

        } else {
            stringStringHashMap.put("message","already exists");
            return stringStringHashMap;
        }
        return stringStringHashMap;
    }

    public boolean emailExists(String email) {
        try {
            return studentService.getStudentByEmail(email) != null;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
