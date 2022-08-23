package com.hotels.repository;

import com.hotels.entities.engine.Engine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EngineRepository extends JpaRepository<Engine, Long> {
    @Query(value = "select * from engine e where e.user_id = :user_id", nativeQuery = true)
    List<Engine> findEnginesByUserId(@Param("user_id") Integer userId);

    @Query(value = "select * from engine e where e.task_id in (select min(task_id) from engine e where e.status = 'new' or e.status = 'NEW')", nativeQuery = true)
    Optional<Engine> findMinimumTaskWithStatusNew();

    @Modifying
    @Query(value = "delete from engine e where e.user_id = :user_id", nativeQuery = true)
    void deleteEngineByUserId(@Param("user_id") Integer userId);
}
