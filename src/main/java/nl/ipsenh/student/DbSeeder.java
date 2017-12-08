package nl.ipsenh.student;

import nl.ipsenh.student.model.Student;
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
    private StudentService studentService;

    @Override
    public void run(String... strings) throws Exception {

        Student student = Student.builder()
                .surName("Hans")
                .middleName("de")
                .lastName("Gans")
                .email("s1088395@student.hsleiden.nl")
                .phoneNumber("0612341234")
                .password("password123")
                .slb("Alex van Manen")
                .build();

        Student student2 = Student.builder()
                .surName("Frans")
                .middleName("de")
                .lastName("Hans")
                .email("s321@student.hsleiden.nl")
                .phoneNumber("0612341234")
                .password("password123567")
                .slb("Michiel Boere")
                .build();

        this.studentService.deleteAllStudents();

        studentService.createStudent(student);
        studentService.createStudent(student2);
    }
}
