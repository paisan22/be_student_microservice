package nl.ipsenh.student;

import nl.ipsenh.student.service.StudentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;

import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceApplicationTests {

	@Autowired
	private StudentService studentService;

	@Test
	public void hashPasswordTest() throws NoSuchAlgorithmException {

		String password = "password123";
		String hash = "482C811DA5D5B4BC6D497FFA98491E38";

		String result = studentService.hashPassword(password);
		String result2 = studentService.hashPassword(password);

		Assert.assertThat(result, is(result2));
		Assert.assertThat(result, is(hash));
	}
}
