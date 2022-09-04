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
import com.hotels.entities.task.status.TaskStatus;
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
    private final TaskStatusRepository taskStatusRep;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRep;
    private final AssignmentsRepository assignmentsRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public AssignmentServiceImpl(
            TaskRepository taskRep, TaskStatusRepository taskStatusRep,
            RoomRepository roomRepository, ReservationRepository reservationRep,
            AssignmentsRepository assignmentsRepository, HotelRepository hotelRepository) {
        this.taskRep = taskRep;
        this.taskStatusRep = taskStatusRep;
        this.roomRepository = roomRepository;
        this.reservationRep = reservationRep;
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
    public TaskStatus getTaskStatus(Long taskId) throws EntityNotFoundException {
        Optional<Task> statusOpt = this.taskRep.findById(taskId);
        statusOpt.orElseThrow(() -> new EntityNotFoundException("Task " + taskId + " status not found."));
        return statusOpt.get().getStatus();
    }

    @Override
    public AssignmentDTO getAssignment(Long taskId) throws NoAssignmentsForTaskException {
        Optional<AssignmentsDB> assignmentDB = this.assignmentsRepository.findAssignmentByTaskId(taskId);
        assignmentDB.orElseThrow(() -> new NoAssignmentsForTaskException("There are no finished assignments for task: " + taskId));
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
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void runTasks() {
        Optional<Task> task;
        synchronized (this) {
            // Once in every X seconds, check all the NEW tasks and run them.
            task = this.taskRep.findMinimumTaskWithStatusNew();
            if (!task.isPresent()) {
                return; // no new status tasks
            }
            task.get().getStatus().setStatusStr(MyConstants.TASK_IN_PROGRESS);
            this.taskStatusRep.save(task.get().getStatus());
        }
        //System.out.println("Thread: " + Thread.currentThread().getId() + " started calculation.");
        executeNewTask(task.get());
        TaskStatus newStatus = this.taskStatusRep.findById(task.get().getStatus().getStatusId()).get(); // Get updated task with observer params
        newStatus.setStatusStr(MyConstants.TASK_DONE); // Set status to done
        this.taskStatusRep.save(newStatus);
        //System.out.println("Thread: " + Thread.currentThread().getId() + " finished calculation.");
    }

    @Transactional
    public void executeNewTask(Task task) {
        try {
            Assignment assignment = runEngine(task);
            AssignmentsDB assignmentDB = convertAssignmentToAssignmentDB(assignment);
            assignmentDB.setTaskId(task.getTaskId());
            AssignmentEvaluator assignmentEvaluator = new AssignmentEvaluator();
            assignmentDB.setFitness(assignmentEvaluator.getFitness(assignment, null));
            this.assignmentsRepository.save(assignmentDB);
            System.out.println(assignment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static AssignmentsDB convertAssignmentToAssignmentDB(Assignment assignment) {
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
        return assignmentDB;
    }

    private AssignmentDTO toDto(AssignmentsDB assignmentsDB) throws EntityNotFoundException {
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        Optional<Task> taskOpt = this.taskRep.findById(assignmentsDB.getTaskId());
        taskOpt.orElseThrow(() -> new EntityNotFoundException("Task " + assignmentsDB.getTaskId() + " not found."));
        long hotelId = taskOpt.get().getHotelId();
        Optional<Hotel> hotelOpt = this.hotelRepository.findById((int) hotelId);
        hotelOpt.orElseThrow(() -> new EntityNotFoundException("Hotel with id " + hotelId + " not found."));

        assignmentDTO.setHotelId((int) hotelId);
        for (ReservationRoomAssignment reservationRoomAssignment : assignmentsDB.getReservationRoomAssignments()) {
            assignmentDTO.getReservationRoomMap().put(
                    Math.toIntExact(reservationRoomAssignment.getReservationNumber()),
                    Math.toIntExact(reservationRoomAssignment.getRoomNumber()));
        }
        return assignmentDTO;
    }

    private Assignment runEngine(Task task) {
        List<Room> roomList = roomRepository.findRoomsByHotelIdAndAvailableDate((int) task.getHotelId(), task.getDate());
        List<Reservation> reservationList = reservationRep.findReservationsByHotelIdAndCheckinDate((int) task.getHotelId(), task.getDate());

        return Main.getAssignment(
                task.getTaskId(),
                convertTaskToTaskProperties(task),
                roomList,
                reservationList);
    }

    private TaskProperties convertTaskToTaskProperties(Task task) throws EntityNotFoundException {
        TaskProperties taskProperties = new TaskProperties();
        taskProperties.setMutationProb(new Probability(task.getMutationProb()));
        taskProperties.setSelectionStrategy(getSelectionStrategy(task.getSelectionStrategy(), task.getSelecDouble()));
        taskProperties.setTermCond(
                getTerminationConditions(
                        task.getTerminationInts(),
                        task.getMaxDuration(),
                        task.getGenerationCount(),
                        task.getGenerationLimit(),
                        task.getNaturalFitness(),
                        task.getTargetFitness()
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
            Integer selectionStrategyInteger, Double selectionDouble) {
        switch (selectionStrategyInteger) {
            case 1:
                return new RankSelection();
            case 3:
                return new SigmaScaling();
            case 4:
                return new StochasticUniversalSampling();
            case 5:
                return new TournamentSelection(new Probability(selectionDouble)); // Has 2 b: 0.5 < d < 1.0
            case 6:
                return new TruncationSelection(selectionDouble); // Has 2 b: 0.0 < d < 1.0
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
            int[] termConditions, // Array of the termination conditions ints as described above.
            Long maxDuration, // maxDuration for ElapsedTime.
            Integer generationCount, // generationCount for GenerationCount.
            Integer generationLimit, // generationLimit for GenerationCount.
            Boolean naturalFitness, // naturalFitness for Stagnation and TargetFitness.
            Double targetFitness // targetFitness for TargetFitness.
    ) {
        List<TerminationCondition> termList = new ArrayList<>();
        for (int i : termConditions) {
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
