package com.hotels.service;

import com.hotels.entities.engine.Engine;
import com.hotels.entities.engine.EngineDTO;
import com.hotels.entities.hotel.Hotel;
import com.hotels.entities.user.User;
import com.hotels.exceptions.CannotUpdateTaskNotNewException;
import com.hotels.repository.EngineRepository;
import com.hotels.repository.HotelRepository;
import com.hotels.repository.UserRepository;
import com.hotels.utils.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EngineServiceImpl implements EngineService {
    @Value("${naturalFitness}")
    Boolean naturalFitness;
    private final EngineRepository engineRep;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public EngineServiceImpl(EngineRepository engineRep, UserRepository userRepository, HotelRepository hotelRepository) {
        this.engineRep = engineRep;
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public EngineDTO getEngineByTaskId(Long taskId) throws EntityNotFoundException {
        Optional<Engine> engineOpt = this.engineRep.findById(taskId);
        engineOpt.orElseThrow(() -> new EntityNotFoundException("Engine Data for task id: " + taskId + " not found!"));
        return toDto(engineOpt.get());
    }

    @Override
    public List<EngineDTO> getEnginesByUserId(Integer userId) throws EntityNotFoundException {
        List<Engine> engines = this.engineRep.findEnginesByUserId(userId);
        if (engines.size() == 0) {
            throw new EntityNotFoundException("Engine Data for user id: " + userId + " not found!");
        }
        return engines.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public long insertEngineData(EngineDTO engineDTO) {
        Optional<User> userOpt = this.userRepository.findById(engineDTO.getUserId());
        userOpt.orElseThrow(() -> new EntityNotFoundException("User with id: " + engineDTO.getUserId() + " not found!"));
        Optional<Hotel> hotelOpt = this.hotelRepository.findById(userOpt.get().getHotel().getId());
        hotelOpt.orElseThrow(() -> new EntityNotFoundException("Hotel with id " + userOpt.get().getHotel().getId() + " not found."));
        Engine savedEngine = this.engineRep.save(toEngine(engineDTO, false));
        return savedEngine.getTaskId();
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void updateEngineData(EngineDTO engineDTO) {
        Optional<Engine> engineOpt = this.engineRep.findById(engineDTO.getTaskId());
        if (engineOpt.get().getStatus().compareToIgnoreCase(MyConstants.TASK_NEW) != 0) {
            throw new CannotUpdateTaskNotNewException("Task number " + engineDTO.getTaskId() + " is not new. Can't update in progress\\running tasks.");
        }
        this.engineRep.save(toEngine(engineDTO, true));
    }

    @Override
    public void deleteEngineDataByTaskId(Long taskId) throws EntityNotFoundException {
        this.engineRep.deleteById(taskId);
    }

    @Override
    @Transactional
    public void deleteEngineDataByUserId(Integer userId) {
        this.engineRep.deleteEngineByUserId(userId);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private Engine toEngine(EngineDTO engineDTO, boolean setTaskId) {
        Engine engine = new Engine();
        if (setTaskId) {
            engine.setTaskId(engineDTO.getTaskId());
        }
        User user = this.userRepository.findById(engineDTO.getUserId()).get();
        engine.setUser(user);
        engine.setHotel(user.getHotel());
        engine.setStatus(MyConstants.TASK_NEW);
        engine.setDate(LocalDate.parse(engineDTO.getDate()));
        engine.setNaturalFitness(this.naturalFitness);
        engine.setMutationProb(engineDTO.getMutationProb());
        engine.setSelectionStrategy(engineDTO.getSelectionStrategy());
        engine.setSelecDouble(engineDTO.getSelecdouble());
        engine.setMaxDuration(engineDTO.getMaxDuration());
        engine.setGenerationCount(engineDTO.getGenerationCount());
        engine.setGenerationLimit(engineDTO.getGenerationLimit());
        engine.setTargetFitness(engineDTO.getTargetFitness());
        engine.setTerminationElapsedTime(engineDTO.getTerminationElapsedTime());
        engine.setTerminationGenerationCount(engineDTO.getTerminationGenerationCount());
        engine.setTerminationStagnation(engineDTO.getTerminationStagnation());
        engine.setTerminationTargetFitness(engineDTO.getTerminationTargetFitness());
        engine.setTerminationUserAbort(engineDTO.getTerminationUserAbort());
        return engine;
    }

    private EngineDTO toDto(Engine engine) {
        EngineDTO engineDTO = new EngineDTO();
        engineDTO.setTaskId(engine.getTaskId());
        engineDTO.setUserId(engine.getUserId());
        engineDTO.setDate(engine.getDate() != null ? engine.getDate().toString() : null);
        engineDTO.setMutationProb(engine.getMutationProb());
        engineDTO.setSelectionStrategy(engine.getSelectionStrategy());
        engineDTO.setSelecdouble(engine.getSelecDouble());
        engineDTO.setMaxDuration(engine.getMaxDuration());
        engineDTO.setGenerationCount(engine.getGenerationCount());
        engineDTO.setGenerationLimit(engine.getGenerationLimit());
        engineDTO.setTargetFitness(engine.getTargetFitness());
        engineDTO.setTerminationElapsedTime(engine.getTerminationElapsedTime());
        engineDTO.setTerminationGenerationCount(engine.getTerminationGenerationCount());
        engineDTO.setTerminationStagnation(engine.getTerminationStagnation());
        engineDTO.setTerminationTargetFitness(engine.getTerminationTargetFitness());
        engineDTO.setTerminationUserAbort(engine.getTerminationUserAbort());
        return engineDTO;
    }
}
