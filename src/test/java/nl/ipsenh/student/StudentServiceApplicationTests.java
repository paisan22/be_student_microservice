package nl.ipsenh.student;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.AssertTrue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceApplicationTests {

	@Test
	public void contextLoads() {
		assertTrue(false);
		assertFalse(true);
		assertEquals(1, 0);
	}

}
