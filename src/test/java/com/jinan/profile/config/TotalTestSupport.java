package com.jinan.profile.config;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Profile("test")
@Transactional
@SpringBootTest
public abstract class TotalTestSupport {



}
