package com.hotels.service;

import com.hotels.entities.engine.Engine;
import com.hotels.entities.user.User;
import com.hotels.repository.EngineRepository;
import com.hotels.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
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
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Engine getEngineDataByUserId(Integer userId) throws EntityNotFoundException {
        checkValidUserId(userId);
        return this.engineRep.findById(userId).get();
    }

    @Override
    public void insertEngineData(Engine engine) {
        Optional<User> userOpt = this.userRepository.findById(engine.getUserId());
        userOpt.orElseThrow(() -> new EntityNotFoundException("User with id: " + engine.getUserId() + " not found!"));
        this.engineRep.save(engine);
    }

    @Override
    public void updateEngineData(Engine engine) {
        checkValidUserId(engine.getUserId());
        this.engineRep.save(engine);
    }

    @Override
    @Transactional
    public void deleteEngineData(Integer userId) {
        this.engineRep.deleteEngineByUserId(userId);
    }

    private void checkValidUserId(int userId) {
        Optional<Engine> engineOptional = this.engineRep.findById(userId);
        engineOptional.orElseThrow(() -> new EntityNotFoundException("Engine Data for user id: " + userId + " not found!"));
    }
}
