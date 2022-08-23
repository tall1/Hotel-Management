package com.hotels.service;

import com.hotels.Main;
import com.hotels.assignment.evolutionary.entities.AssignmentEvaluator;
import com.hotels.entities.assignment.Assignment;
import com.hotels.entities.assignment.AssignmentDTO;
import com.hotels.entities.assignment.db.AssignmentsDB;
import com.hotels.entities.assignment.db.ReservationRoomAssignment;
import com.hotels.entities.engine.Engine;
import com.hotels.entities.engine.EngineProperties;
import com.hotels.entities.hotel.Hotel;
import com.hotels.entities.reservation.Reservation;
import com.hotels.entities.room.Room;
import com.hotels.exceptions.NoAssignmentsForTaskException;
import com.hotels.repository.*;
import com.hotels.utils.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.selection.*;
import org.uncommons.watchmaker.framework.termination.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    private final EngineRepository engineRep;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRep;
    private final UserRepository userRep;
    private final AssignmentsRepository assignmentsRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public AssignmentServiceImpl(
            EngineRepository engineRep,
            RoomRepository roomRepository,
            ReservationRepository reservationRep,
            UserRepository userRep, AssignmentsRepository assignmentsRepository, HotelRepository hotelRepository) {
        this.engineRep = engineRep;
        this.roomRepository = roomRepository;
        this.reservationRep = reservationRep;
        this.userRep = userRep;
        this.assignmentsRepository = assignmentsRepository;
        this.hotelRepository = hotelRepository;
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
    public String getTaskStatus(Long taskId) throws EntityNotFoundException {
        Optional<Engine> engineOpt = this.engineRep.findById(taskId);
        engineOpt.orElseThrow(() -> new EntityNotFoundException("Task " + taskId + " not found."));
        return engineOpt.get().getStatus();
    }

    @Override
    public AssignmentDTO getAssignment(Long taskId) throws NoAssignmentsForTaskException {
        Optional<AssignmentsDB> assignmentDB = this.assignmentsRepository.findAssignmentByTaskId(taskId);
        assignmentDB.orElseThrow(() -> new NoAssignmentsForTaskException(taskId.toString()));
        return toDto(assignmentDB.get());
    }

    @Override
    @Transactional
    public void updateRoomsAvailableDate(AssignmentDTO chosenAssignment) throws EntityNotFoundException {
        // Update rooms available date according to chosen assignment:
        Map<Integer, Integer> reservationRoomMap = chosenAssignment.getReservationRoomMap();
        int hotelId = chosenAssignment.getHotelId();
        for (Map.Entry<Integer, Integer> entry : reservationRoomMap.entrySet()) {
            LocalDate reservationCheckoutDate = getReservationCheckoutDateByResNum(entry.getKey());
            Integer roomId = getRoomIdByHotelIdAndRoomNumber(hotelId, entry.getValue());
            this.roomRepository.updateRoomAvailableDate(roomId, reservationCheckoutDate);
        }
    }

    @Override
    public void runTasks() {
        Optional<Engine> engine;
        synchronized (this) {
            // Once in every X seconds, check all the NEW tasks and run them.
            engine = this.engineRep.findMinimumTaskWithStatusNew();
            if (!engine.isPresent()) {
                return; // no new status tasks
            }
            engine.get().setStatus(MyConstants.TASK_IN_PROGRESS);
            this.engineRep.save(engine.get());
        }
        executeNewTask(engine);
        this.engineRep.save(engine.get());
    }

    @Transactional
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void executeNewTask(Optional<Engine> engine) {
        try {
            int hotelId = findHotelByUserId(engine.get().getUserId());
            Assignment assignment = runEngine(getEnginePropertiesByTaskId(engine.get().getTaskId()), engine.get().getDate(), hotelId);
            AssignmentsDB assignmentDB = new AssignmentsDB();
            // convert assignment to AssignmentDB
            for (Map.Entry<Reservation, Room> entity : assignment.getReservationRoomMap().entrySet()) {
                ReservationRoomAssignment reservationRoomAssignment = new ReservationRoomAssignment();
                reservationRoomAssignment.setAssignment(assignmentDB);
                reservationRoomAssignment.setReservationNumber((long) entity.getKey().getReservationNumber());
                reservationRoomAssignment.setRoomId((long) entity.getValue().getId());
                assignmentDB.getReservationRoomAssignments().add(reservationRoomAssignment);
            }
            assignmentDB.setTaskId(engine.get().getTaskId());
            AssignmentEvaluator assignmentEvaluator = new AssignmentEvaluator();
            assignmentDB.setFitness(assignmentEvaluator.getFitness(assignment, null));
            this.assignmentsRepository.save(assignmentDB);
            engine.get().setStatus(MyConstants.TASK_DONE);
            System.out.println(assignment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AssignmentDTO toDto(AssignmentsDB assignmentsDB) throws EntityNotFoundException {
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        Optional<Engine> engineOpt = this.engineRep.findById(assignmentsDB.getTaskId());
        engineOpt.orElseThrow(() -> new EntityNotFoundException("Task " + assignmentsDB.getTaskId() + " not found."));
        int hotelId = engineOpt.get().getHotel().getId();
        Optional<Hotel> hotelOpt = this.hotelRepository.findById(hotelId);
        hotelOpt.orElseThrow(() -> new EntityNotFoundException("Hotel with id " + hotelId + " not found."));
        assignmentDTO.setHotelId(hotelId);
        for (ReservationRoomAssignment reservationRoomAssignment : assignmentsDB.getReservationRoomAssignments()) {
            assignmentDTO.getReservationRoomMap().put(
                    Math.toIntExact(reservationRoomAssignment.getReservationNumber()),
                    Math.toIntExact(reservationRoomAssignment.getRoomId()));
        }
        return assignmentDTO;
    }

    private Assignment runEngine(EngineProperties engineProperties, LocalDate date, Integer hotelId) {
        List<Room> roomList = roomRepository.findRoomsByHotelIdAndAvailableDate(hotelId, date);
        List<Reservation> reservationList = reservationRep.findReservationsByHotelIdAndCheckinDate(hotelId, date);

        return Main.getAssignment(
                engineProperties,
                roomList,
                reservationList);
    }

    private EngineProperties getEnginePropertiesByTaskId(Long taskId) throws EntityNotFoundException {
        Optional<Engine> engine = this.engineRep.findById(taskId);
        engine.orElseThrow(() -> new EntityNotFoundException("Engine properties for task " + taskId + " not found!"));
        EngineProperties engineProperties = new EngineProperties();
        engineProperties.setMutationProb(new Probability(engine.get().getMutationProb()));
        engineProperties.setSelectionStrategy(getSelectionStrategy(engine.get().getSelectionStrategy(), engine.get().getSelecDouble()));
        engineProperties.setTermCond(
                getTerminationConditions(
                        engine.get().getTerminationInts(),
                        engine.get().getMaxDuration(),
                        engine.get().getGenerationCount(),
                        engine.get().getGenerationLimit(),
                        engine.get().getNaturalFitness(),
                        engine.get().getTargetFitness()
                ));
        return engineProperties;
    }

    /* SelectionStrategy Integers:
     * 1. RankSelection
     * 2. RouletteWheelSelection
     * 3. SigmaScaling
     * 4. StochasticUniversalSampling
     * 5. TournamentSelection
     * 6. TruncationSelection
     * */
    private SelectionStrategy<Object> getSelectionStrategy(
            Integer selectionStrategyInteger, Double selecDouble) {
        switch (selectionStrategyInteger) {
            case 1:
                return new RankSelection();
            case 3:
                return new SigmaScaling();
            case 4:
                return new StochasticUniversalSampling();
            case 5:
                return new TournamentSelection(new Probability(selecDouble)); // Has 2 b: 0.5 < d < 1.0
            case 6:
                return new TruncationSelection(selecDouble); // Has 2 b: 0.0 < d < 1.0
            default:
                return new RouletteWheelSelection();
        }
    }

    /* TerminationCondition Integers:
     * 1. ElapsedTime
     * 2. GenerationCount
     * 3. Stagnation
     * 4. TargetFitness
     * 5. UserAbort
     * */
    private List<TerminationCondition> getTerminationConditions(
            int[] termConds, // Array of the termination conditions ints as described above.
            Long maxDuration, // maxDuration for ElapsedTime.
            Integer generationCount, // generationCount for GenerationCount.
            Integer generationLimit, // generationLimit for GenerationCount.
            Boolean naturalFitness, // naturalFitness for Stagnation and TargetFitness.
            Double targetFitness // targetFitness for TargetFitness.
    ) {
        List<TerminationCondition> termList = new ArrayList<>();
        for (int i : termConds) {
            switch (i) {
                case 1:
                    termList.add(new ElapsedTime(maxDuration));
                    break;
                case 2:
                    termList.add(new GenerationCount(generationCount));
                    break;
                case 3:
                    termList.add(new Stagnation(generationLimit, naturalFitness));
                    break;
                case 4:
                    termList.add(new TargetFitness(targetFitness, naturalFitness));
                    break;
                case 5:
                    termList.add(new UserAbort());
                    break;
                default:
                    break;
            }
        }
        return termList;
    }

    private int findHotelByUserId(Integer userId) throws EntityNotFoundException {
        Optional<Integer> hotelIdOpt = this.userRep.findHotelIdByUserId(userId);
        hotelIdOpt.orElseThrow(() -> new EntityNotFoundException("AssignmentServiceImpl: User with id " + userId + " not found!"));
        return hotelIdOpt.get();
    }

    private LocalDate getReservationCheckoutDateByResNum(int reservationNum) throws EntityNotFoundException {
        Optional<LocalDate> checkoutOpt = reservationRep.findCheckoutDateByReservationNumber(reservationNum);
        checkoutOpt.orElseThrow(() -> new EntityNotFoundException("Reservation" + reservationNum + " not found."));
        return checkoutOpt.get();
    }

    private Integer getRoomIdByHotelIdAndRoomNumber(int hotelId, int roomNum) throws EntityNotFoundException {
        Optional<Integer> roomIdOpt = roomRepository.findRoomByRoomNumberAndHotelId(hotelId, roomNum);
        roomIdOpt.orElseThrow(() -> new EntityNotFoundException("Room " + roomNum + " in hotel " + hotelId + "not found."));
        return roomIdOpt.get();
    }
}
