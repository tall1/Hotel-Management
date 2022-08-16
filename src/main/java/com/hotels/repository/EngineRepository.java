package com.hotels.repository;

import com.hotels.service.utils.EngineDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface EngineRepository extends JpaRepository<EngineDTO, Integer> {
    @Transactional
    @Modifying
    @Query(value = "delete from engine e where e.user_id = :user_id", nativeQuery = true)
    void deleteEngineByUserId(@Param("user_id") Integer userId);
}
