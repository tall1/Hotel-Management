package com.hotels.service;

import com.hotels.entities.task.TaskDTO;
import com.hotels.exceptions.CannotUpdateTaskNotNewException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    TaskDTO getTaskByTaskId(Long taskId) throws EntityNotFoundException;

    List<TaskDTO> getTasksByUserId(Integer userId) throws EntityNotFoundException;

    List<TaskDTO> getTasksByUserIdAndDate(Integer userId, LocalDate date);

    long insertTaskData(TaskDTO taskDTO) throws EntityNotFoundException;

    void updateTaskData(TaskDTO taskDTO) throws EntityNotFoundException, CannotUpdateTaskNotNewException;

    void deleteTaskDataByTaskId(Long taskId) throws EntityNotFoundException;

    void deleteTaskDataByUserId(Integer userId) throws EntityNotFoundException;
}
