package com.jackssybin.demo03.config;

import com.jackssybin.demo.config.BaseConfig;
import com.jackssybin.demo.entity.DemoUser;
import com.jackssybin.demo03.mapper.DemoUserMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowStep;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisJobConfiguration extends BaseConfig {
    @Autowired
    private DemoUserMapper demoUserMapper;


    @Bean
    public Job mybatisJob(){

        return jobBuilderFactory.get("mybatisJob")

                .start(mybatisStep())
                .next(flowStep())
//                .next(mybatisStep2())
//                .next(mybatisStep3())
                .build()
                ;
    }


    @Bean
    public Flow flow1() {
        return new FlowBuilder<SimpleFlow>("flow1")
                .start(mybatisStep2())
                .next(mybatisStep3())
                .build();
    }
    public Step flowStep(){
        FlowStep flowStep =new FlowStep(flow1());
        flowStep.setJobRepository(jobRepository);
        return flowStep;
    }
    public Step mybatisStep(){
        return stepBuilderFactory.get("mybatisStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        DemoUser demoUser = demoUserMapper.queryUser(12);
                        System.out.println("demoUser=="+demoUser);
                        return null;
                    }
                }).build();
    }

    public Step mybatisStep2(){
        return stepBuilderFactory.get("mybatisStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        DemoUser demoUser = demoUserMapper.queryUser(12);
                        System.out.println("mybatisStep2 =demoUser=="+demoUser);
                        return null;
                    }
                }).build();
    }

    public Step mybatisStep3(){
        return stepBuilderFactory.get("mybatisStep3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        DemoUser demoUser = demoUserMapper.queryUser(12);
                        System.out.println("mybatisStep3 =demoUser=="+demoUser);
                        return null;
                    }
                }).build();
    }


}
