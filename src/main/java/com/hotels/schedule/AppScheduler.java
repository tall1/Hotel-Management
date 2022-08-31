package com.hotels.schedule;

import com.hotels.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class AppScheduler {
    private final AssignmentService assignmentService;

    @Autowired
    public AppScheduler(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Scheduled(fixedRate = 3000)
    @Async
    public void schedule() {
        assignmentService.runTasks();
    }
}
