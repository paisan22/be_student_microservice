package nl.ipsenh.student.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by paisanrietbroek on 03/10/2017.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Students")
public class Student {

    @Id
    private String id;

    private String surName;
    private String middleName;
    private String lastName;

    @Indexed(unique = true)
    private String email;


    private String password;

    private String phoneNumber;
    private String slb;

}
