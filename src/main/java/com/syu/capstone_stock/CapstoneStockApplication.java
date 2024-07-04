package com.syu.capstone_stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CapstoneStockApplication {
    public static void main(String[] args) {
        SpringApplication.run(CapstoneStockApplication.class, args);
    }
}
