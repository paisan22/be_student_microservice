package nl.ipsenh.student.service;

import nl.ipsenh.student.model.Student;
import nl.ipsenh.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by paisanrietbroek on 03/10/2017.
 */

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student createStudent(Student student) {
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

    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public void deleteAll() {
        studentRepository.deleteAll();
    }
}
