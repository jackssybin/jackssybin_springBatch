package com.jackssybin.demo02.writer;

import com.jackssybin.demo02.entity.DemoUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class DemoWriter<T> implements ItemWriter<T> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    @Transactional
    public void write(List<? extends T> list) throws Exception {
        for(T a:list){
            DemoUser demoUser=(DemoUser)a;
        }
        logger.info("DemoWriter==={}",list);

    }


}