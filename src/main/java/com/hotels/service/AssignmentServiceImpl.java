package com.hotels.service;

import com.hotels.Main;
import com.hotels.assignment.Assignment;
import com.hotels.service.utils.EngineProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    @Autowired
    private Main myMain; // TODO: FIND OUT HOW TO INPUT USER DATA IN HERE

    @PostConstruct
    public void postConstructor() {
        // here put any after construction operations
        System.out.println("AssignmentServiceImpl: @PostConstruct");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("AssignmentServiceImpl: @PreDestroy");
    }

    @Override
    public Assignment getAssignment(EngineProperties engineProp) {
        // Return small assignment...
        return myMain.getAssignment();
    }
}
