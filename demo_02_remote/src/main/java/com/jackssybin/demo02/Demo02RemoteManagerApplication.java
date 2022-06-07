package com.jackssybin.demo02;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@EnableBatchProcessing
public class Demo02RemoteManagerApplication {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active","remotePartitionManager");
        ConfigurableApplicationContext ctx=SpringApplication.run(Demo02RemoteManagerApplication.class, args);
        exec(ctx,null,"remotePartitioningJob");
    }
    public static void exec(ConfigurableApplicationContext ctx,
                            Job job,
                            String jobName){
        JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
        if(null==job){
            job =ctx.getBean(jobName, Job.class);
        }
        JobParametersBuilder jobParametersBuilder =new JobParametersBuilder();
        jobParametersBuilder.addDate("date",new Date());
        jobParametersBuilder.addString("rnd", UUID.randomUUID().toString());
        try {
            jobLauncher.run(job,jobParametersBuilder.toJobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}