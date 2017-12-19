package nl.ipsenh.student;

import nl.ipsenh.student.repository.StudentRepository;
import nl.ipsenh.student.service.StudentService;
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

    @Autowired
    private StudentService studentService;

    @Override
    public void run(String... strings) throws Exception {
        // development purpose
        this.studentService.resetStudentsForDevelopment();
    }
}
