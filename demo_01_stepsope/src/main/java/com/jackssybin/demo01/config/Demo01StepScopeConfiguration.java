package com.jackssybin.demo01.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Demo01StepScopeConfiguration {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job demo01StepScopeJob(){
        return jobBuilderFactory.get("demo01StepScopeJob")
                .start(demo01StepScopeStep())
                .build();
    }

    // 改造如下
    public Step demo01StepScopeStep(){
        return stepBuilderFactory.get("demo01StepScopeStep")
                .tasklet(demo01StepScopeTasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public Tasklet demo01StepScopeTasklet(@Value("#{jobParameters[rnd]} ") String rnd){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                logger.info("=======demo01StepScopeTasklet======rnd:{}",rnd);
                return null;
            }
        };
    }

}
