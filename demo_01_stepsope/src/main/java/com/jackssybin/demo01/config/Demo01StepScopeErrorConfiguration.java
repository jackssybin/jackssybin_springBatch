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
public class Demo01StepScopeErrorConfiguration {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job demo01StepScopeJobError(){
        return jobBuilderFactory.get("demo01StepScopeJobError")
                .start(demo01StepScopeStepError(null))
                .build();
    }

    /**
     ** 这个时候在 Step （demo01StepScopeStep） 中添加注解stepscope。
     ** Scope 'step' is not active for the current thread. 这个说的也很明白，step还没有装载初始化完成呢。
     ** 所以只有在step激活，即装载成功之后才能获取@Value 这种情况。我们可以把taskle 定义成个bean来获取
     **/
    @Bean
    @StepScope
    public Step demo01StepScopeStepError(@Value("${demo01.param.name}") String paramName){
        return stepBuilderFactory.get("demo01StepScopeStepError")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info("=======demo01StepScopeStepError======paramName:{}",paramName);
                        return null;
                    }
                }).build();
    }


}
