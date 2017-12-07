package nl.ipsenh.student.API;

import nl.ipsenh.student.model.Student;
import nl.ipsenh.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
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

    @GetMapping(value = "/all")
    public List<Student> getAll() {
        return studentService.getAllStudents();
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) throws NoSuchAlgorithmException {
        return studentService.createStudent(student);
    }

    @PutMapping
    public Student updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable("id") String id) {
        studentService.deleteStudent(id);
    }

    @GetMapping
    public Student getStudentByEmail(@RequestHeader("email") String email) {
        return studentService.getStudentByEmail(email);
    }
}
