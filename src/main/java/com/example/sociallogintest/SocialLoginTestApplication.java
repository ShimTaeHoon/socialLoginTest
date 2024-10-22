package com.example.sociallogintest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SocialLoginTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialLoginTestApplication.class, args);
    }

}
