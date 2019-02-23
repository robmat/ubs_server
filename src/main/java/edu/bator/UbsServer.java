package edu.bator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("edu.bator")
@EnableScheduling
@EnableAsync
public class UbsServer {
    public static void main(String[] args) {
        SpringApplication.run(UbsServer.class, args);
    }
}
