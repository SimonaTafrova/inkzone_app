package com.example.inkzone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InkZoneApplication {

    public static void main(String[] args) {
        SpringApplication.run(InkZoneApplication.class, args);
    }

}
