package com.example.library;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@Slf4j
public class LibraryApplication {

    public static void main(String[] args) {
        log.info("Starting Library application...");
        SpringApplication.run(LibraryApplication.class, args);
        log.info("Library application started");
    }

}
