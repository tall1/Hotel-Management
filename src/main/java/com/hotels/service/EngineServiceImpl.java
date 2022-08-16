package com.hotels.service;

import com.hotels.repository.EngineRepository;
import com.hotels.entities.engine.EngineDTO;
import com.hotels.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class EngineServiceImpl implements EngineService {
    private final EngineRepository engineRep;
    private final UserRepository userRepository;

    @Autowired
    public EngineServiceImpl(EngineRepository engineRep, UserRepository userRepository) {
        this.engineRep = engineRep;
        this.userRepository = userRepository;
    }

    @Override
    public EngineDTO getEngineDataByUserId(Integer userId) throws EntityNotFoundException {
        Optional<EngineDTO> engineDTO = engineRep.findById(userId);
        if (!engineDTO.isPresent()) { // TODO: make this in method.
            throw new EntityNotFoundException("Engine Data for user id: " + userId + " not found!");
        }
        return engineDTO.get();
    }

    @Override
    public void insertEngineData(EngineDTO engineDTO) {
        if (!this.userRepository.findById(engineDTO.getUserId()).isPresent()) {
            throw new EntityNotFoundException("User with id: " + engineDTO.getUserId() + " not found!");
        }
        this.engineRep.save(engineDTO);
    }

    @Override
    public void updateEngineData(EngineDTO engineDTO) {
        Optional<EngineDTO> engineDTOOptional = this.engineRep.findById(engineDTO.getUserId());
        if (!engineDTOOptional.isPresent()) {
            throw new EntityNotFoundException("Engine Data for user id: " + engineDTO.getUserId() + " not found!");
        }
        this.engineRep.save(engineDTO);
    }

    @Override
    public void deleteEngineData(Integer userId) {
        Optional<EngineDTO> engineDTOOptional = this.engineRep.findById(userId);
        if (!engineDTOOptional.isPresent()) {
            throw new EntityNotFoundException("Engine Data for user id: " + userId + " not found!");
        }
        this.engineRep.deleteEngineByUserId(userId);
    }
}
