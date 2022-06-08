package com.jackssybin.demo.config;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseConfig {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public  StepBuilderFactory stepBuilderFactory;
    @Autowired
    public JobRepository jobRepository;
}
