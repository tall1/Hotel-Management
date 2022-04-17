
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

public class AssignmentMutation implements EvolutionaryOperator<Assignment> {

    private List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private NumberGenerator<Probability> mutationProbability;

    public AssignmentMutation(List<Room> rooms, List<Reservation> reservations, Probability mutationProbability) {
        this(rooms, reservations, new ConstantGenerator(mutationProbability));
    }

    public AssignmentMutation(List<Room> rooms, List<Reservation> reservations, NumberGenerator<Probability> mutationProbability) {
//        this.rooms = (List<Room>) ((ArrayList<Room>) rooms).clone();
//        this.reservations = (List<Reservation>) ((ArrayList<Reservation>) reservations).clone();


        this.rooms.addAll(rooms);
        this.reservations.addAll(reservations);


        this.mutationProbability = mutationProbability;
    }

    public List<Assignment> apply(List<Assignment> selectedCandidates, Random rng) {
        List<Assignment> mutatedPopulation = new ArrayList(selectedCandidates.size());
        Iterator i$ = selectedCandidates.iterator();

        while (i$.hasNext()) {
            Assignment assignment = (Assignment) i$.next();
            // mutatedPopulation.add(new Assignment(assignment.getRoomNo(), rng.nextInt(rooms.size())));
            mutatedPopulation.add(this.mutateAssignment(assignment, rng));
        }

        return mutatedPopulation;
    }

    private Assignment mutateAssignment(Assignment oldAssignment, Random rng) {
//        StringBuilder buffer = new StringBuilder(s);
//        Assignment newAssignment = new Assignment(oldAssignment.getRoomNo(), oldAssignment.getReservationRoomNo());
////        for(int i = 0; i < buffer.length(); ++i) {
//        if (((Probability) this.mutationProbability.nextValue()).nextEvent(rng)) {
////                buffer.setCharAt(i, this.alphabet[rng.nextInt(this.alphabet.length)]);
//            newAssignment.setRoomNo(rng.nextInt(this.rooms.size()-1));
//        }
//    //}
//
//        return newAssignment;
        return null;
}
}
