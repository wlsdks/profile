package com.jinan.profile.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // auditing기능 추가(활성화 시킴)
@Configuration
public class JpaConfig {

}
