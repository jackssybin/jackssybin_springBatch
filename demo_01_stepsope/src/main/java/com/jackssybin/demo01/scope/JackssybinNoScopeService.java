package com.jackssybin.demo01.scope;

import org.springframework.stereotype.Service;

@Service
public class JackssybinNoScopeService {

    public String getMessage() {
        return "Hello World!"+this.hashCode();
    }
}
