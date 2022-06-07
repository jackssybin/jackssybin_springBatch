package com.jackssybin.demo02;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.UUID;

public class BatchJobRunTest {
    @Autowired
    ApplicationContext ctx;
    @Autowired
    JobLauncher jobLauncher;

    void executeJobByName(String jobName){
        executeJobByName(jobName,null);
    }

    void executeJobByName(String jobName, Job job){
        if(null==job){
            job =ctx.getBean(jobName,Job.class);
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
