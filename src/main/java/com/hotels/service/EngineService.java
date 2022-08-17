package com.hotels.service;

import com.hotels.entities.engine.Engine;

import javax.persistence.EntityNotFoundException;

public interface EngineService {

    Engine getEngineDataByUserId(Integer userId) throws EntityNotFoundException;

    void insertEngineData(Engine engine) throws EntityNotFoundException;

    void updateEngineData(Engine engine) throws EntityNotFoundException;

    void deleteEngineData(Integer userId) throws EntityNotFoundException;
}
