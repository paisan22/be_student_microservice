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
                .phoneNumber("0612341234")
                .password("password123")
                .cohort(2017)
                .mentor("Alex van Manen")
                .build();

        Student student2 = Student.builder()
                .sureName("Frans")
                .middleName("de")
                .lastName("Hans")
                .email("s321@student.hsleiden.nl")
                .phoneNumber("0612341234")
                .password("password123")
                .cohort(2018)
                .mentor("Michiel Boere")
                .build();

        Student student3 = Student.builder()
                .sureName("Hans3")
                .middleName("de")
                .lastName("Gans")
                .email("s123@student.hsleiden.nl")
                .number("s1235")
                .phoneNumber("0612341234")
                .password("password123")
                .build();

        this.studentRepository.deleteAll();

        this.studentRepository.save(student);
        this.studentRepository.save(student2);
        this.studentRepository.save(student3);
    }
}
