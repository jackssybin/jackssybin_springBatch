package com.jackssybin.demo.reader;

import com.jackssybin.demo.entity.DemoUser;
import org.springframework.batch.item.ItemReader;
import org.springframework.lang.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

public class DemoReader implements ItemReader<DemoUser> {

    private int limit = 10;
    private static AtomicInteger errorCounter = new AtomicInteger();
    private int counter = 0;
    Object lock = new Object();

    public DemoReader(int limit) {
        this.limit = limit;
    }

    @Nullable
    @Override
    public DemoUser read() throws Exception {

        synchronized (lock){
            if (counter < limit) {
                DemoUser user =new DemoUser();
                user.setAge(18);
                user.setName("name"+counter);
                counter++;
                return user;
            }
        }

        return null;
    }

    /**
     * @param limit number of items that will be generated
     * (null returned on consecutive calls).
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getCounter() {
        return counter;
    }

    public int getLimit() {
        return limit;
    }

    public void resetCounter()
    {
        this.counter = 0;
    }
}