package nl.ipsenh.student.service;

import nl.ipsenh.student.model.Student;
import nl.ipsenh.student.repository.StudentRepository;
import nl.ipsenh.student.service.email.EmailService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
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

    private static final String MESSAGE = "message";

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public HashMap<String, String> createStudent(Student student) throws NoSuchAlgorithmException, DuplicateKeyException {

        HashMap<String, String> studentHashmap = this.createStudentHashmap(student);

        try {
            student.setPassword(hashPassword(student.getPassword()));
            studentRepository.insert(student);

            emailService.sendRegistrationMail(student.getEmail());

            studentHashmap.put(MESSAGE, "true");

        } catch (DuplicateKeyException | NullPointerException e) {
            studentHashmap.put(MESSAGE, e.getMessage());
        }

        return studentHashmap;
    }

    public HashMap<String, String> createStudentHashmap(Student student) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put(MESSAGE, "false");
        stringStringHashMap.put("email", student.getEmail());

        return stringStringHashMap;
    }

    public Student updateStudent(Student student) throws NoSuchAlgorithmException {

        Student one = studentRepository.findByEmail(student.getEmail());

        one.setEmail(student.getEmail());
        one.setLastName(student.getLastName());
        one.setSurName(student.getSurName());
        one.setMiddleName(student.getMiddleName());
        one.setSlbEmail(student.getSlbEmail());
        one.setPhoneNumber(student.getPhoneNumber());

        if (student.getPassword().isEmpty()) {
            one.setPassword(one.getPassword());
        } else {
            one.setPassword(hashPassword(student.getPassword()));
        }

        studentRepository.save(one);
        return one;
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
            return false;
        }
    }

    public String getRandomPassword() {
        String characters =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
        return RandomStringUtils.random( 15, characters );
    }

    public void resetStudentsForDevelopment() throws NoSuchAlgorithmException {
//        Student student = Student.builder()
//                .surName("Hans")
//                .lastName("Gans")
//                .email("s1088395@student.hsleiden.nl")
//                .phoneNumber("0612341234")
//                .password("password123")
//                .slbEmail("alex@mail.nl")
//                .build();



        Student student2 = Student.builder()
                .surName("Mike")
                .middleName("de")
                .lastName("Hans")
                .email("s1098641@student.hsleiden.nl")
                .phoneNumber("0612341236")
                .password("password123")
                .slbEmail("michiel@mail.nl")
                .build();

        Student student3 = Student.builder()
                .surName("Gerben")
                .middleName("de")
                .lastName("Hans")
                .email("s1085142@student.hsleiden.nl")
                .phoneNumber("0612341232")
                .password("admin")
                .slbEmail("roland@mail.nl")
                .build();

        this.studentRepository.deleteAll();

        //        student.setPassword(hashPassword(student.getPassword()));
        //        studentRepository.save(student);

        student2.setPassword(hashPassword(student2.getPassword()));
        student3.setPassword(hashPassword(student3.getPassword()));

        studentRepository.save(student2);
        studentRepository.save(student3);
    }

}