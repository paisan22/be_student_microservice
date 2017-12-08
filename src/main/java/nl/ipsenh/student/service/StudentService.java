package nl.ipsenh.student.service;

import nl.ipsenh.student.model.Student;
import nl.ipsenh.student.repository.StudentRepository;
import nl.ipsenh.student.service.email.EmailService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by paisanrietbroek on 03/10/2017.
 */

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student createStudent(Student student) throws NoSuchAlgorithmException {

        student.setPassword(hashPassword(student.getPassword()));

        studentRepository.insert(student);
        return student;
    }

    public Student updateStudent(Student student) {
        studentRepository.save(student);
        return student;
    }

    public void deleteStudent(String id) {
        studentRepository.delete(id);
    }

    public void deleteAllStudents() {
        studentRepository.deleteAll();
    }

    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    public String hashPassword(String password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter
                .printHexBinary(digest).toUpperCase();
    }

    public boolean resetPassword(String email) throws NoSuchAlgorithmException {

        try {
            Student byEmail = studentRepository.findByEmail(email);

            String randomPassword = getRandomPassword();

            byEmail.setPassword(hashPassword(randomPassword));

            emailService.sendNewPassword(email, randomPassword);

            studentRepository.save(byEmail);

            return true;
        }
        catch (NullPointerException | MailAuthenticationException exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    private String getRandomPassword() {
        String characters =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
        return RandomStringUtils.random( 15, characters );
    }
}