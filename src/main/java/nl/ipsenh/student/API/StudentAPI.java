package nl.ipsenh.student.API;

import nl.ipsenh.student.DbSeeder;
import nl.ipsenh.student.model.Student;
import nl.ipsenh.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by paisanrietbroek on 02/10/2017.
 */

@RestController
@RequestMapping(value = "/students")
@CrossOrigin(value = "*")
public class StudentAPI {

    @Autowired
    private StudentService studentService;

    @PostMapping(value = "/reset")
    public boolean resetUser() throws Exception {

        DbSeeder dbSeeder = new DbSeeder();
        dbSeeder.run();

        return true;
    }

    @GetMapping(value = "/all")
    public List<Student> getAll() {
        return studentService.getAllStudents();
    }

    @PostMapping
    public ResponseEntity createStudent(@RequestBody Student student) throws NoSuchAlgorithmException {

        HashMap<String, String> student1 = studentService.createStudent(student);

        return ResponseEntity.ok().body(student1);
    }

    @PutMapping
    public Student updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    public  boolean deleteStudent(@PathVariable("id") String id) {

        try {
            studentService.deleteStudent(id);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    @GetMapping
    public Student getStudentByEmail(@RequestHeader("email") String email) {
        return studentService.getStudentByEmail(email);
    }
}
