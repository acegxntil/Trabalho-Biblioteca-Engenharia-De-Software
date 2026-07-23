package com.sgbu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SgbuApplication {

    public static void main(String[] args) {
        SpringApplication.run(SgbuApplication.class, args);
    }
}
