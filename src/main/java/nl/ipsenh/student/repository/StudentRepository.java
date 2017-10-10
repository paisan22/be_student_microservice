package nl.ipsenh.student.repository;

import nl.ipsenh.student.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by paisanrietbroek on 03/10/2017.
 */

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {

    Student findByNumber(String number);
}
