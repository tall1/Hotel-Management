package com.hotels.service;

import com.hotels.repository.EngineRepository;
import com.hotels.service.utils.EngineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class EngineServiceImpl implements EngineService {
    private final EngineRepository engineRep;

    @Autowired
    public EngineServiceImpl(EngineRepository engineRep) {
        this.engineRep = engineRep;
    }

    @Override
    public EngineDTO getEngineDataByUserId(Integer userId) throws EntityNotFoundException{
        Optional<EngineDTO> engineDTO = engineRep.findById(userId);
        if(!engineDTO.isPresent()){
            throw new EntityNotFoundException("Engine Data Not Found!");
        }
        return engineDTO.get();
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
        this.engineRep.deleteEngineByUserId(userId);
    }
}
