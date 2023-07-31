package com.jinan.profile;

import com.jinan.profile.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Import(TestSecurityConfig.class)
@SpringBootTest
class ProfileApplicationTests {

	@Test
	void contextLoads() {
	}


}
