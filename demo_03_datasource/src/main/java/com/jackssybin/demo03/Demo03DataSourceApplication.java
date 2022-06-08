package com.jackssybin.demo03;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;
import java.util.UUID;

@SpringBootApplication
@EnableBatchProcessing
@MapperScan("com.jackssybin.demo03.mapper")
public class Demo03DataSourceApplication {
    public static void main(String[] args) {
//        ConfigurableApplicationContext ctx=SpringApplication.run(Demo03DataSourceApplication.class, args);
//        exec(ctx,null,"remotePartitioningJob");

        byte b1=1,b2=2,b3,b6;
        final byte b4=4,b5=6;
//        b6=(b4+b5);b3=(b1+b2);
//        System.out.println(b3+b6);
        TreeSet<Integer> integers = new TreeSet<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return -(o1-o2);
            }
        });
        integers.add(5);
        integers.add(2);
        integers.add(15);
        integers.add(15);
        integers.add(-4);
        System.out.println(integers);
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