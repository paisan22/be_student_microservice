package nl.ipsenh.student.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import nl.ipsenh.student.model.Student;
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

    public HashMap<String, String> Authententicate(String email, String password)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {

        HashMap<String, String> loginStatus = new HashMap<>();
        loginStatus.put("email", "false");
        loginStatus.put("password", "false");

        // check if email exist
        Student studentByEmail = studentService.getStudentByEmail(email);

        if ((studentByEmail != null) && (studentByEmail instanceof Student)) {
            loginStatus.put("email", "true");
        }

        loginStatus.put("password", isCorrectPassword(studentByEmail, password));

        if (isCorrectLogin(loginStatus)) {
            loginStatus.put("token", createToken(studentByEmail));
        }

        return loginStatus;
    }

    public String isCorrectPassword(Student student, String password) throws NoSuchAlgorithmException {
        try {
            return String.valueOf(Objects.equals(student.getPassword(), studentService.hashPassword(password)));
        } catch (NullPointerException e) {
            return "false";
        }

    }

    public boolean isCorrectLogin(HashMap<String, String> hashmap) {
        String email = hashmap.get("email");
        String password = hashmap.get("password");

        return (email == "true" && password == "true");
    }

    public String createToken(Student student) throws JWTCreationException, UnsupportedEncodingException {

        Algorithm algorithm = Algorithm.HMAC256("secret");

        HashMap<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("student", student.getEmail());

        return JWT.create()
                .withClaim("email", student.getEmail())
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

            DecodedJWT jwt = jwtVerifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public boolean resetPassword(String email) throws NoSuchAlgorithmException {
        return studentService.resetPassword(email);
    }

    public boolean emailExists(String email) {
        try {
            return studentService.getStudentByEmail(email) != null;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
