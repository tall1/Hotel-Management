package com.hotels.schedule;

import com.hotels.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AppScheduler {
    private final AssignmentService assignmentService;

    @Autowired
    public AppScheduler(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Scheduled(fixedDelay = 3000)
    public void schedule() {
        //System.out.println("Hello");
        assignmentService.runTasks();
    }
}
