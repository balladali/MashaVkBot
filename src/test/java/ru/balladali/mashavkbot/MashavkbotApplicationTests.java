package ru.balladali.mashavkbot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"credential.vk.group-id=123"})
public class MashavkbotApplicationTests {

	@Test
	public void contextLoads() {
	}

}

