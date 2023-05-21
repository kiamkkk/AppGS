package com.gseek.gs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GsApplication.class, args);
    }

}
