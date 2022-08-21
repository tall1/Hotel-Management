package com.hotels.controller;

import com.hotels.entities.engine.EngineDTO;
import com.hotels.service.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/engine")
public class EngineController {
    EngineService engineService;

    @Autowired
    public EngineController(EngineService engineService) {
        this.engineService = engineService;
    }

    @GetMapping("/{taskId}")
    public EngineDTO getEngine(@PathVariable Long taskId) throws EntityNotFoundException {
        return engineService.getEngineByTaskId(taskId);
    }

    //    localhost:8080/api/v1/person/get1/5
    @GetMapping("/get_engines_by_user/{userId}")
    public List<EngineDTO> getEngines(@PathVariable Integer userId) throws EntityNotFoundException {
        return engineService.getEnginesByUserId(userId);
    }

    @PostMapping
    public long insertEngineProperties(@RequestBody EngineDTO engineDTO) throws EntityNotFoundException {
        return engineService.insertEngineData(engineDTO);
    }

    @PutMapping
    public void updateEngineProperties(@RequestBody EngineDTO engineDTO) throws EntityNotFoundException {
        engineService.updateEngineData(engineDTO);
    }

    @DeleteMapping("/delete_by_task")
    public void deleteEngineProperties(Long taskId) throws EntityNotFoundException {
        engineService.deleteEngineDataByTaskId(taskId);
    }

    @DeleteMapping("/delete_by_user")
    public void deleteEngineProperties(Integer userId) throws EntityNotFoundException {
        engineService.deleteEngineDataByUserId(userId);
    }
}

