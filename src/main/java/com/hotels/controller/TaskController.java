package com.hotels.controller;

import com.hotels.entities.task.TaskDTO;
import com.hotels.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/task")
public class TaskController {
    TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{taskId}")
    public TaskDTO getTask(@PathVariable Long taskId) throws EntityNotFoundException {
        return taskService.getTaskByTaskId(taskId);
    }

    //    localhost:8080/api/v1/person/get1/5
    @GetMapping("/get_tasks_by_user/{userId}")
    public List<TaskDTO> getTasks(@PathVariable Integer userId) throws EntityNotFoundException {
        return taskService.getTasksByUserId(userId);
    }

    @PostMapping
    public long insertTaskProperties(@RequestBody TaskDTO taskDTO) throws EntityNotFoundException {
        return taskService.insertTaskData(taskDTO);
    }

    @PutMapping
    public void updateTaskProperties(@RequestBody TaskDTO taskDTO) throws EntityNotFoundException {
        taskService.updateTaskData(taskDTO);
    }

    @DeleteMapping("/delete_by_task")
    public void deleteTaskProperties(Long taskId) throws EntityNotFoundException {
        taskService.deleteTaskDataByTaskId(taskId);
    }

    @DeleteMapping("/delete_by_user")
    public void deleteTaskProperties(Integer userId) throws EntityNotFoundException {
        taskService.deleteTaskDataByUserId(userId);
    }
}

