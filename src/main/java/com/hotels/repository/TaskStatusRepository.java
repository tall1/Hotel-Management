package com.hotels.repository;

import com.hotels.entities.task.status.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
}
