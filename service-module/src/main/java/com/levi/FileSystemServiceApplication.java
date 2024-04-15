package com.levi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.levi.mapper"})
public class FileSystemServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileSystemServiceApplication.class, args);
    }
}
