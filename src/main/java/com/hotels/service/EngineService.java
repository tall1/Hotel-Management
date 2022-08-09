package com.hotels.service;

import com.hotels.service.utils.EngineDTO;

import java.sql.SQLException;

public interface EngineService {

    EngineDTO getEngineDataByUserId(Integer userId) throws SQLException;

    void insertEngineData(EngineDTO engineDTO) throws SQLException;

    void updateEngineData(EngineDTO engineDTO) throws SQLException;

    void deleteEngineData(Integer userId) throws SQLException;
}
