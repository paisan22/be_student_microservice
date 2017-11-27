package nl.ipsenh.student;

import nl.ipsenh.student.model.Student;
import nl.ipsenh.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by paisanrietbroek on 03/10/2017.
 */

@Component
public class DbSeeder implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void run(String... strings) throws Exception {

        Student student = Student.builder()
                .sureName("Hans")
                .middleName("de")
                .lastName("Gans")
                .email("s123@student.hsleiden.nl")
                .number("s123")
                .phoneNumber("0612341234")
                .password("password123")
                .build();

        Student student2 = Student.builder()
                .sureName("Hans2")
                .middleName("de")
                .lastName("Gans")
                .email("s1233@student.hsleiden.nl")
                .number("s123")
                .phoneNumber("0612341234")
                .password("password123")
                .build();

        this.studentRepository.deleteAll();

        this.studentRepository.save(student);
        this.studentRepository.save(student2);

    }
}
