package com.sigma.gclc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sigma.gclc.service.LoadImageRepositoryScheduledTask;

@Configuration
@ComponentScan
@EnableScheduling
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(new Object[]{ Application.class,LoadImageRepositoryScheduledTask.class}, args);
    }
}
