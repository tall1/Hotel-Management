package com.hotels.service;

import com.hotels.entities.engine.EngineDTO;
import com.hotels.exceptions.CannotUpdateTaskNotNewException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface EngineService {

    EngineDTO getEngineByTaskId(Long taskId) throws EntityNotFoundException;

    List<EngineDTO> getEnginesByUserId(Integer userId) throws EntityNotFoundException;

    long insertEngineData(EngineDTO engineDTO) throws EntityNotFoundException;

    void updateEngineData(EngineDTO engineDTO) throws EntityNotFoundException, CannotUpdateTaskNotNewException;

    void deleteEngineDataByTaskId(Long taskId) throws EntityNotFoundException;

    void deleteEngineDataByUserId(Integer userId) throws EntityNotFoundException;
}
