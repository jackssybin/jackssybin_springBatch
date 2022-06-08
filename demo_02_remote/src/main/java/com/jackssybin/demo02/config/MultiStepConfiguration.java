package com.jackssybin.demo02.config;

import com.jackssybin.demo.entity.DemoUser;
import com.jackssybin.demo.reader.DemoReader;
import com.jackssybin.demo.writer.DemoWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class MultiStepConfiguration {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Bean
    public TaskExecutor muliTaskExecutor(){
        return new SimpleAsyncTaskExecutor("spring_batch_mult");
    }

    @Bean
    public Job sampleMutliJob(){
        return jobBuilderFactory.get("sampleMutliJob")
                .start(sampleMutliStep())
                .build();
    }

    @Bean
    public Step sampleMutliStep() {
        return this.stepBuilderFactory.get("sampleMutliStep")
                .<DemoUser, DemoUser>chunk(2)
                .reader(new DemoReader(10))
                .writer(new DemoWriter<>())
                .taskExecutor(muliTaskExecutor())
                .throttleLimit(5)
                .build();
    }
}
