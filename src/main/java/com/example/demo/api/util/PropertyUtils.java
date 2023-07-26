package com.example.demo.api.util;

import org.springframework.core.env.Environment;

public class PropertyUtils {
    private static Environment environment(){
        // 1. BeanContext 의 applicationContext 에서 바로 Environment 의 객체를 받아온다.
        return BeanContext.get(Environment.class);
    }

    public static String getProperty(String key){
        return environment().getProperty(key);
    }
}
