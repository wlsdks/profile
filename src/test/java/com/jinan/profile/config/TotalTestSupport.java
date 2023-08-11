package com.jinan.profile.config;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@Import(TestSecurityConfig.class) // 테스트 설정 클래스 적용
@SpringBootTest
public abstract class TotalTestSupport {

}
