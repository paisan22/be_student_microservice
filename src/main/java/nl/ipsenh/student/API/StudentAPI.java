package nl.ipsenh.student.API;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by paisanrietbroek on 02/10/2017.
 */

@RestController
public class StudentAPI {

    @RequestMapping(value = "/test")
    public String test() {
        return "Student api called!";
    }

}
