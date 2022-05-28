package com.jackssybin.demo01.config;


import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.*;

public class JackssybinScope implements Scope {
    private final ThreadLocal threadScope = new ThreadLocal() {
        protected Object initialValue() {
            return new HashMap();
        }
    };
    @Override
    public Object get(String name, ObjectFactory objectFactory) {
        Map scope = (Map) threadScope.get();
        Object object = scope.get(name);
        if(object==null) {
            object = objectFactory.getObject();
            scope.put(name, object);
        }
        return object;
    }
    public Object remove(String name) {
        Map scope = (Map) threadScope.get();
        return scope.remove(name);
    }
    public void registerDestructionCallback(String name, Runnable callback) {
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    public String getConversationId() {
        // TODO Auto-generated method stub
        return null;
    }
}