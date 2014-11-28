package com.sigma.gclc;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sigma.gclc.service.LoadImageRepositoryScheduledTask;

@Configuration
@ComponentScan
@EnableScheduling
@EnableAutoConfiguration
public class Application {
	
	@Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("2048KB");
        factory.setMaxRequestSize("512KB");
        return factory.createMultipartConfig();
    }

    public static void main(String[] args) {
        SpringApplication.run(new Object[]{ Application.class,LoadImageRepositoryScheduledTask.class}, args);
    }
}
