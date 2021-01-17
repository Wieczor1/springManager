package com.manager.appmanager;

import com.manager.appmanager.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppmanagerApplication.class, args);
    }
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAllExported();
            storageService.init();
        };

    }
}
