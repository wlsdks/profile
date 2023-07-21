package com.jinan.profile;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@Profile("test")
@SpringBootTest
class ProfileApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("print test");
	}


}
