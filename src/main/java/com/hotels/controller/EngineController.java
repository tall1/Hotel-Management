package com.hotels.controller;

import com.hotels.service.EngineService;
import com.hotels.service.utils.EngineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping(path = "/engine")
public class EngineController {
    EngineService engineService;

    @Autowired
    public EngineController(EngineService engineService) {
        this.engineService = engineService;
    }

    //    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{userId}")
    public EngineDTO getEngineDTO(@PathVariable Integer userId) throws SQLException {
        return engineService.getEngineDataByUserId(userId);
    }

    @PostMapping
    public void insertEngineProperties(@RequestBody EngineDTO eDto) throws SQLException {
        engineService.insertEngineData(eDto);
    }

    @PutMapping
    public void updateEngineProperties(@RequestBody EngineDTO eDto) throws SQLException {
        engineService.updateEngineData(eDto);
    }

    @DeleteMapping
    public void deleteEngineProperties(@RequestBody Integer userId) throws SQLException {
        engineService.deleteEngineData(userId);
    }
}

