package com.jackssybin.demo01.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.*;

/**
 * 定义在同一个线程内，多次获取同一个bean 获取的是同一个。
 * 如果不用该注解 获取的是不同的bean
 */
public class JackssybinScope implements Scope {

    private final ThreadLocal<Map<String, Object>> threadLoacal = new ThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };

    public Object get(String name, ObjectFactory<?> objectFactory) {
        Map<String, Object> scope = threadLoacal.get();
        Object obj = scope.get(name);

        // 不存在则放入ThreadLocal
        if (obj == null) {
            obj = objectFactory.getObject();
            scope.put(name, obj);

            System.out.println("Not exists " + name + "; hashCode: " + obj.hashCode());
        } else {
            System.out.println("Exists " + name + "; hashCode: " + obj.hashCode());
        }

        return obj;
    }

    public Object remove(String name) {
        Map<String, Object> scope = threadLoacal.get();
        return scope.remove(name);
    }

    public String getConversationId() {
        return null;
    }

    public void registerDestructionCallback(String arg0, Runnable arg1) {
    }

    public Object resolveContextualObject(String arg0) {
        return null;
    }

}

