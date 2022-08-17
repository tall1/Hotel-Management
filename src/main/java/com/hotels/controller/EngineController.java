package com.hotels.controller;

import com.hotels.entities.engine.Engine;
import com.hotels.service.EngineService;
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
    public Engine getEngine(@PathVariable Integer userId) throws EntityNotFoundException {
        return engineService.getEngineDataByUserId(userId);
    }

    @PostMapping
    public void insertEngineProperties(@RequestBody Engine engine) throws EntityNotFoundException {
        engineService.insertEngineData(engine);
    }

    @PutMapping
    public void updateEngineProperties(@RequestBody Engine engine) throws EntityNotFoundException {
        engineService.updateEngineData(engine);
    }

    @DeleteMapping
    public void deleteEngineProperties(Integer userId) throws EntityNotFoundException {
        engineService.deleteEngineData(userId);
    }
}

