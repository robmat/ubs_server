package edu.bator.config.async;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class AsyncConfig extends WebMvcConfigurationSupport {
    @Bean
    public Executor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}