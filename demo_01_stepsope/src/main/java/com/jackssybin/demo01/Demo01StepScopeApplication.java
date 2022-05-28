package com.jackssybin.demo01;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class Demo01StepScopeApplication {
    public static void main(String[] args) {
        SpringApplication.run(Demo01StepScopeApplication.class, args);
    }
}