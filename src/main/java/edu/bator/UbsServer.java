package edu.bator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@ComponentScan
@EnableScheduling
@EnableAsync
@EnableWebSecurity
@EnableWebSocketMessageBroker
public class UbsServer {
    public static void main(String[] args) {
        SpringApplication.run(UbsServer.class, args);
    }
}
