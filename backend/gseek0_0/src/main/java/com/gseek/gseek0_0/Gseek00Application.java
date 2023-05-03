package com.gseek.gseek0_0;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude= {SecurityAutoConfiguration.class })
public class Gseek00Application {

    public static void main(String[] args) {
        SpringApplication.run(Gseek00Application.class, args);
    }

}
