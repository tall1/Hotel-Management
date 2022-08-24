package com.hotels.service;

import com.hotels.Main;
import com.hotels.assignment.evolutionary.entities.AssignmentEvaluator;
import com.hotels.entities.assignment.Assignment;
import com.hotels.entities.assignment.AssignmentDTO;
import com.hotels.entities.assignment.db.AssignmentsDB;
import com.hotels.entities.assignment.db.ReservationRoomAssignment;
import com.hotels.entities.task.Task;
import com.hotels.entities.task.TaskProperties;
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
    private final TaskRepository taskRep;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRep;
    private final UserRepository userRep;
    private final AssignmentsRepository assignmentsRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public AssignmentServiceImpl(
            TaskRepository taskRep,
            RoomRepository roomRepository,
            ReservationRepository reservationRep,
            UserRepository userRep, AssignmentsRepository assignmentsRepository, HotelRepository hotelRepository) {
        this.taskRep = taskRep;
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
        Optional<Task> taskOpt = this.taskRep.findById(taskId);
        taskOpt.orElseThrow(() -> new EntityNotFoundException("Task " + taskId + " not found."));
        return taskOpt.get().getStatus();
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
        Optional<Task> task;
        synchronized (this) {
            // Once in every X seconds, check all the NEW tasks and run them.
            task = this.taskRep.findMinimumTaskWithStatusNew();
            if (!task.isPresent()) {
                return; // no new status tasks
            }
            task.get().setStatus(MyConstants.TASK_IN_PROGRESS);
            this.taskRep.save(task.get());
        }
        executeNewTask(task);
        this.taskRep.save(task.get());
    }

    @Transactional
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void executeNewTask(Optional<Task> task) {
        try {
            int hotelId = findHotelByUserId(task.get().getUserId());
            Assignment assignment = runEngine(getTaskPropertiesByTaskId(task.get().getTaskId()), task.get().getDate(), hotelId);
            AssignmentsDB assignmentDB = new AssignmentsDB();
            // convert assignment to AssignmentDB
            for (Map.Entry<Reservation, Room> entity : assignment.getReservationRoomMap().entrySet()) {
                ReservationRoomAssignment reservationRoomAssignment = new ReservationRoomAssignment();
                reservationRoomAssignment.setAssignment(assignmentDB);
                reservationRoomAssignment.setReservationNumber((long) entity.getKey().getReservationNumber());
                //reservationRoomAssignment.setRoomId((long) entity.getValue().getId()); // Number or Id?
                reservationRoomAssignment.setRoomNumber((long) entity.getValue().getRoomNumber());
                assignmentDB.getReservationRoomAssignments().add(reservationRoomAssignment);
            }
            assignmentDB.setTaskId(task.get().getTaskId());
            AssignmentEvaluator assignmentEvaluator = new AssignmentEvaluator();
            assignmentDB.setFitness(assignmentEvaluator.getFitness(assignment, null));
            this.assignmentsRepository.save(assignmentDB);
            task.get().setStatus(MyConstants.TASK_DONE);
            System.out.println(assignment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AssignmentDTO toDto(AssignmentsDB assignmentsDB) throws EntityNotFoundException {
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        Optional<Task> taskOpt = this.taskRep.findById(assignmentsDB.getTaskId());
        taskOpt.orElseThrow(() -> new EntityNotFoundException("Task " + assignmentsDB.getTaskId() + " not found."));
        int hotelId = taskOpt.get().getHotel().getId();
        Optional<Hotel> hotelOpt = this.hotelRepository.findById(hotelId);
        hotelOpt.orElseThrow(() -> new EntityNotFoundException("Hotel with id " + hotelId + " not found."));

        assignmentDTO.setHotelId(hotelId);
        for (ReservationRoomAssignment reservationRoomAssignment : assignmentsDB.getReservationRoomAssignments()) {
            assignmentDTO.getReservationRoomMap().put(
                    Math.toIntExact(reservationRoomAssignment.getReservationNumber()),
                    Math.toIntExact(reservationRoomAssignment.getRoomNumber()));
        }
        return assignmentDTO;
    }

    private Assignment runEngine(TaskProperties taskProperties, LocalDate date, Integer hotelId) {
        List<Room> roomList = roomRepository.findRoomsByHotelIdAndAvailableDate(hotelId, date);
        List<Reservation> reservationList = reservationRep.findReservationsByHotelIdAndCheckinDate(hotelId, date);

        return Main.getAssignment(
                taskProperties,
                roomList,
                reservationList);
    }

    private TaskProperties getTaskPropertiesByTaskId(Long taskId) throws EntityNotFoundException {
        Optional<Task> task = this.taskRep.findById(taskId);
        task.orElseThrow(() -> new EntityNotFoundException("Task properties for task " + taskId + " not found!"));
        TaskProperties taskProperties = new TaskProperties();
        taskProperties.setMutationProb(new Probability(task.get().getMutationProb()));
        taskProperties.setSelectionStrategy(getSelectionStrategy(task.get().getSelectionStrategy(), task.get().getSelecDouble()));
        taskProperties.setTermCond(
                getTerminationConditions(
                        task.get().getTerminationInts(),
                        task.get().getMaxDuration(),
                        task.get().getGenerationCount(),
                        task.get().getGenerationLimit(),
                        task.get().getNaturalFitness(),
                        task.get().getTargetFitness()
                ));
        return taskProperties;
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
