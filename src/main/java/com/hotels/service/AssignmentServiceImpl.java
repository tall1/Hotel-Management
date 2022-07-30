package com.hotels.service;

import com.hotels.Main;
import com.hotels.assignment.SimpleAssignment;
import com.hotels.repository.EngineRepository;
import com.hotels.service.utils.EngineDTO;
import com.hotels.service.utils.EngineProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.SQLException;
import java.util.Optional;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    private final Main myMain;
    private final EngineRepository engineRep;

    @Autowired
    public AssignmentServiceImpl(Main main, EngineRepository engineRep) {
        this.myMain = main;
        this.engineRep = engineRep;
    }

    @PostConstruct
    public void postConstructor() {
        // here put any after construction operations
        System.out.println("AssignmentServiceImpl: @PostConstruct");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("AssignmentServiceImpl: @PreDestroy");
    }

    @Override
    public SimpleAssignment getAssignment(Integer userId) throws SQLException {
        Optional<EngineDTO> eDto = engineRep.findById(userId);
        if (eDto.isPresent()) {
            return new SimpleAssignment(this.myMain.getAssignment(new EngineProperties(eDto.get())));
        } else {
            throw new SQLException("EngineDTO not found");
        }
    }

    @Override
    public void insertEngineData(EngineDTO engineDTO) {
        this.engineRep.save(engineDTO);
    }

    @Override
    public void updateEngineData(EngineDTO engineDTO) {
        this.engineRep.save(engineDTO);
    }

    @Override
    public void deleteEngineData(Integer userId) {
        this.engineRep.deleteById(userId);
    }
}
