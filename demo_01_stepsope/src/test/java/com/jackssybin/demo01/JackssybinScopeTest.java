package com.jackssybin.demo01;

import com.jackssybin.demo01.scope.JackssybinHaveScopeService;
import com.jackssybin.demo01.scope.JackssybinNoScopeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = {Demo01StepScopeApplication.class})
public class JackssybinScopeTest {

    @Autowired
    ApplicationContext ctx;
    @Test
    public void jackssybinScopetest2(){
        JackssybinHaveScopeService service = ctx.getBean(JackssybinHaveScopeService.class);
        System.out.println(service.getMessage()+"="+service.hashCode());
        JackssybinHaveScopeService service2= ctx.getBean(JackssybinHaveScopeService.class);
        System.out.println(service2.getMessage()+"="+service2.hashCode());
        System.out.println("======================");
        JackssybinNoScopeService service3 = ctx.getBean(JackssybinNoScopeService.class);
        System.out.println(service3.getMessage()+"="+service3.hashCode());
        JackssybinNoScopeService service4= ctx.getBean(JackssybinNoScopeService.class);
        System.out.println(service4.getMessage()+"="+service4.hashCode());
    }
}
