package nl.ipsenh.student.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import nl.ipsenh.student.model.Student;
import nl.ipsenh.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by paisanrietbroek on 04/12/2017.
 */

@Service
public class LoginService {

    @Autowired
    private StudentRepository studentRepository;

    public String Authententicate(String email, String password) throws UnsupportedEncodingException {
        if (isCorrectLogin(email, password)) {
            Student student = studentRepository.findByEmail(email);
            return createToken(student);
        }
        return null;
    }

    public boolean isCorrectLogin(String email, String password) {
        Student student = studentRepository.findByEmail(email);
        return Objects.equals(student.getPassword(), password);
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
}
