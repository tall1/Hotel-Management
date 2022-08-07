package com.hotels.repository;
import com.hotels.service.utils.EngineDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EngineRepository extends JpaRepository<EngineDTO, Integer> {
}
