package com.hotels.repository;

import com.hotels.entities.assignment.db.AssignmentsDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AssignmentsRepository extends JpaRepository<AssignmentsDB, Long> {
    @Query(value = "select * from assignments a where a.task_id = :task_id", nativeQuery = true)
    Optional<AssignmentsDB> findAssignmentByTaskId(@Param("task_id") long taskId);
}
