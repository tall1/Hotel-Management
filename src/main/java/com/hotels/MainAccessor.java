package com.hotels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MainAccessor {
    private static MainAccessor instance;
    private final ApplicationContext applicationContext;

    @Autowired
    public MainAccessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void registerInstance(){
        instance = this;
        System.out.println("MainAccessor: @PostConstruct");
    }

    public static <T> T getBean(Class<T> clazz) {
        return instance.applicationContext.getBean(clazz);
    }
}
