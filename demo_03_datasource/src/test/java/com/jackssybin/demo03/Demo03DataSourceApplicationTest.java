package com.jackssybin.demo03;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {Demo03DataSourceApplication.class})
public class Demo03DataSourceApplicationTest extends BatchJobRunTest {

    @Test
    public void step1ManagerJobTest(){
        executeJobByName("mybatisJob");
    }
}
