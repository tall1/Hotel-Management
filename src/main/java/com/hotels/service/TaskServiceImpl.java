package com.hotels.service;

import com.hotels.entities.hotel.Hotel;
import com.hotels.entities.task.Task;
import com.hotels.entities.task.TaskDTO;
import com.hotels.entities.task.status.TaskStatus;
import com.hotels.entities.user.User;
import com.hotels.exceptions.CannotUpdateTaskNotNewException;
import com.hotels.repository.HotelRepository;
import com.hotels.repository.TaskRepository;
import com.hotels.repository.UserRepository;
import com.hotels.utils.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRep;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRep, UserRepository userRepository, HotelRepository hotelRepository) {
        this.taskRep = taskRep;
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public TaskDTO getTaskByTaskId(Long taskId) throws EntityNotFoundException {
        Optional<Task> taskOpt = this.taskRep.findById(taskId);
        taskOpt.orElseThrow(() -> new EntityNotFoundException("Task Data for task id: " + taskId + " not found!"));
        return toDto(taskOpt.get());
    }

    @Override
    public List<TaskDTO> getTasksByUserId(Integer userId) throws EntityNotFoundException {
        List<Task> tasks = this.taskRep.findTasksByUserId(userId);
        if (tasks.size() == 0) {
            throw new EntityNotFoundException("Task Data for user id: " + userId + " not found!");
        }
        return tasks.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getTasksByUserIdAndDate(Integer userId, LocalDate date) {
        List<Task> tasks = this.taskRep.findTasksByUserIdAndDate(userId, date);
        return tasks.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public long insertTaskData(TaskDTO taskDTO) {
        Optional<User> userOpt = this.userRepository.findById(taskDTO.getUserId());
        userOpt.orElseThrow(() -> new EntityNotFoundException("User with id: " + taskDTO.getUserId() + " not found!"));
        Optional<Hotel> hotelOpt = this.hotelRepository.findById(userOpt.get().getHotel().getId());
        hotelOpt.orElseThrow(() -> new EntityNotFoundException("Hotel with id " + userOpt.get().getHotel().getId() + " not found."));
        Task savedTask = this.taskRep.save(toTask(taskDTO, false));
        return savedTask.getTaskId();
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void updateTaskData(TaskDTO taskDTO) {
        Optional<Task> taskOpt = this.taskRep.findById(taskDTO.getTaskId());
        if (taskOpt.get().getStatus().getStatusStr().compareToIgnoreCase(MyConstants.TASK_NEW) != 0) {
            throw new CannotUpdateTaskNotNewException("Task number " + taskDTO.getTaskId() + " is not new. Can't update in progress\\running tasks.");
        }
        this.taskRep.save(toTask(taskDTO, true));
    }

    @Override
    public void deleteTaskDataByTaskId(Long taskId) throws EntityNotFoundException {
        this.taskRep.deleteById(taskId);
    }

    @Override
    @Transactional
    public void deleteTaskDataByUserId(Integer userId) {
        this.taskRep.deleteTaskByUserId(userId);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private Task toTask(TaskDTO taskDTO, boolean setTaskId) {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setStatusStr(MyConstants.TASK_NEW);
        taskStatus.setProgressPercent(0.0);
        taskStatus.setMaxFitness(0.0);
        taskStatus.setCurFitness(0.0);
        taskStatus.setCurGeneration(0);
        taskStatus.setElapsedTime(0L);

        Task task = new Task();
        if (setTaskId) {
            task.setTaskId(taskDTO.getTaskId());
        }
        User user = this.userRepository.findById(taskDTO.getUserId()).get();
        task.setUser(user);
        task.setHotelId(user.getHotel().getId());
        task.setDate(LocalDate.parse(taskDTO.getDate()));
        task.setElitism(taskDTO.getElitism());
        task.setPopulationSize(taskDTO.getPopulationSize());
        task.setMutationProb(taskDTO.getMutationProb());
        task.setSelectionStrategy(taskDTO.getSelectionStrategy());
        task.setSelecDouble(taskDTO.getSelecDouble());
        task.setMaxDuration(taskDTO.getMaxDuration());
        task.setGenerationCount(taskDTO.getGenerationCount());
        task.setGenerationLimit(taskDTO.getGenerationLimit());
        task.setTargetFitness(taskDTO.getTargetFitness());
        task.setTerminationElapsedTime(taskDTO.getTerminationElapsedTime());
        task.setTerminationGenerationCount(taskDTO.getTerminationGenerationCount());
        task.setTerminationStagnation(taskDTO.getTerminationStagnation());
        task.setTerminationTargetFitness(taskDTO.getTerminationTargetFitness());
        task.setTerminationUserAbort(taskDTO.getTerminationUserAbort());

        taskStatus.setTask(task);
        task.setStatus(taskStatus);

        return task;
    }

    private TaskDTO toDto(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId(task.getTaskId());
        taskDTO.setUserId(task.getUserId());
        taskDTO.setDate(task.getDate() != null ? task.getDate().toString() : null);
        taskDTO.setElitism(task.getElitism());
        taskDTO.setPopulationSize(task.getPopulationSize());
        taskDTO.setMutationProb(task.getMutationProb());
        taskDTO.setSelectionStrategy(task.getSelectionStrategy());
        taskDTO.setSelecDouble(task.getSelecDouble());
        taskDTO.setMaxDuration(task.getMaxDuration());
        taskDTO.setGenerationCount(task.getGenerationCount());
        taskDTO.setGenerationLimit(task.getGenerationLimit());
        taskDTO.setTargetFitness(task.getTargetFitness());
        taskDTO.setTerminationElapsedTime(task.getTerminationElapsedTime());
        taskDTO.setTerminationGenerationCount(task.getTerminationGenerationCount());
        taskDTO.setTerminationStagnation(task.getTerminationStagnation());
        taskDTO.setTerminationTargetFitness(task.getTerminationTargetFitness());
        taskDTO.setTerminationUserAbort(task.getTerminationUserAbort());
        return taskDTO;
    }
}
