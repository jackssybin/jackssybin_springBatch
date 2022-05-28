package com.jackssybin.demo01.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("jackssybinScope")
public class JackssybinHaveScopeService {

    public String getMessage() {
        return "Hello World!"+this.hashCode();
    }
}
