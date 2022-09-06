package com.hotels.repository;

import com.hotels.entities.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(value = "select * from task t where t.user_id = :user_id", nativeQuery = true)
    List<Task> findTasksByUserId(@Param("user_id") Integer userId);

    @Query(value = "select * from task t where t.user_id = :user_id and t.date = :date", nativeQuery = true)
    List<Task> findTasksByUserIdAndDate(@Param("user_id") Integer userId, @Param("date") LocalDate date);

    @Query(value =
            "select * " +
                    "from task " +
                    "where task.task_id in " +
                    "(select min(t.task_id) " +
                    "from task t inner join status s using (status_id) " +
                    "where s.status_str = 'new' or s.status_str = 'NEW')", nativeQuery = true)
    Optional<Task> findMinimumTaskWithStatusNew();

    @Modifying
    @Query(value = "delete from task t where t.user_id = :user_id", nativeQuery = true)
    void deleteTaskByUserId(@Param("user_id") Integer userId);
}
