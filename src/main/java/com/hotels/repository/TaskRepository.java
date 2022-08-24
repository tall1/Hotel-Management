package com.hotels.repository;

import com.hotels.entities.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "select * from task t where t.user_id = :user_id", nativeQuery = true)
    List<Task> findTasksByUserId(@Param("user_id") Integer userId);

    @Query(value = "select * from task t where t.task_id in (select min(task_id) from task t where t.status = 'new' or t.status = 'NEW')", nativeQuery = true)
    Optional<Task> findMinimumTaskWithStatusNew();

    @Modifying
    @Query(value = "delete from task t where t.user_id = :user_id", nativeQuery = true)
    void deleteTaskByUserId(@Param("user_id") Integer userId);
}
