package com.smartticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartTicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartTicketApplication.class, args);
    }

}
