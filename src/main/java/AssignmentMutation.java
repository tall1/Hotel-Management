
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

public class AssignmentMutation implements EvolutionaryOperator<Assignment> {

    private List<Room> rooms;
    private List<Reservation> reservations;
    private NumberGenerator<Probability> mutationProbability;

    public AssignmentMutation(List<Room> rooms, List<Reservation> reservations, Probability mutationProbability) {
        this(rooms, reservations, (NumberGenerator) (new ConstantGenerator(mutationProbability)));
    }

    public AssignmentMutation(List<Room> rooms, List<Reservation> reservations, NumberGenerator<Probability> mutationProbability) {
//        this.rooms = (List<Room>) ((ArrayList<Room>) rooms).clone();
//        this.reservations = (List<Reservation>) ((ArrayList<Reservation>) reservations).clone();
        if (this.rooms == null){
            this.rooms = new ArrayList<>();
        }
        if (this.reservations == null){
            this.reservations = new ArrayList<>();
        }
        rooms.stream().forEach(r->this.rooms.add(r));
        reservations.stream().forEach(r->this.reservations.add(r));


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
        Assignment newAssignment = new Assignment(oldAssignment.getRoomNo(), oldAssignment.getReservationRoomNo());
//        for(int i = 0; i < buffer.length(); ++i) {
        if (((Probability) this.mutationProbability.nextValue()).nextEvent(rng)) {
//                buffer.setCharAt(i, this.alphabet[rng.nextInt(this.alphabet.length)]);
            newAssignment.setRoomNo(rng.nextInt(this.rooms.size()-1));
        }
    //}

        return newAssignment;
}
}
