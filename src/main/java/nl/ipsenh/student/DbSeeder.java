package nl.ipsenh.student;

import nl.ipsenh.student.model.Student;
import nl.ipsenh.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by paisanrietbroek on 03/10/2017.
 */

@Component
public class DbSeeder implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void run(String... strings) throws Exception {
        Student student1 = Student.builder()
                .name("Gerben")
                .number("s123")
                .build();

        Student student2 = Student.builder()
                .name("Mike")
                .number("s321")
                .build();

        Student student3 = Student.builder()
                .name("Paisan")
                .number("s312")
                .build();

        this.studentRepository.deleteAll();

        List<Student> students = Arrays.asList(student1, student2, student3);
        this.studentRepository.save(students);
    }
}
