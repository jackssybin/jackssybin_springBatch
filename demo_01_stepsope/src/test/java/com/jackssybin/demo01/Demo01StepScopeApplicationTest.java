package com.jackssybin.demo01;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {Demo01StepScopeApplication.class})
public class Demo01StepScopeApplicationTest extends BatchJobRunTest {

    @Test
    public void demo01StepScopeJobTest(){
        executeJobByName("demo01StepScopeJob");
    }
}
