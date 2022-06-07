package com.jackssybin.demo02;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.SimplePartitioner;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {Demo02RemoteManagerApplication.class})
public class Demo02RemoteManagerApplicationTest extends BatchJobRunTest {

    @Test
    public void sampleMutliJobTest()
    {
        executeJobByName("sampleMutliJob");
    }

    @Test
    public void remoteChunkingJobTest(){
        executeJobByName("remoteChunkingJob");
    }

    @Test
    public void remotePartitioningJobTest(){
        System.setProperty("spring.profiles.active","remotePartitionMixed");
        executeJobByName("remotePartitioningJob");
    }

    @Test
    public void remotePartitioningDbPollJobTest(){
        System.setProperty("spring.profiles.active","remotePartitionDBManager");
        executeJobByName("remotePartitioningDBPollJob");
    }

    @Test
    public void testSimplePartitionerTest(){
        Partitioner partitioner = new SimplePartitioner();
        System.out.println(partitioner.partition(3));
    }

    @Test
    public void step1ManagerJobTest(){
        executeJobByName("step1ManagerJob");
    }
}
