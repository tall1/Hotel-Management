package com.hotels.service;

import com.hotels.service.utils.EngineDTO;

import javax.persistence.EntityNotFoundException;

public interface EngineService {

    EngineDTO getEngineDataByUserId(Integer userId) throws EntityNotFoundException;

    void insertEngineData(EngineDTO engineDTO) throws EntityNotFoundException;

    void updateEngineData(EngineDTO engineDTO) throws EntityNotFoundException;

    void deleteEngineData(Integer userId) throws EntityNotFoundException;
}
