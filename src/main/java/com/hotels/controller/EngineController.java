package com.hotels.controller;

import com.hotels.service.EngineService;
import com.hotels.entities.engine.EngineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

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
    public EngineDTO getEngineDTO(@PathVariable Integer userId) throws EntityNotFoundException {
        return engineService.getEngineDataByUserId(userId);
    }

    @PostMapping
    public void insertEngineProperties(@RequestBody EngineDTO eDto) throws EntityNotFoundException {
        engineService.insertEngineData(eDto);
    }

    @PutMapping
    public void updateEngineProperties(@RequestBody EngineDTO eDto) throws EntityNotFoundException {
        engineService.updateEngineData(eDto);
    }

    @DeleteMapping
    public void deleteEngineProperties(Integer userId) throws EntityNotFoundException {
        engineService.deleteEngineData(userId);
    }
}

