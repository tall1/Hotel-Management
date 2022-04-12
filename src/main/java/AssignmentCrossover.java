import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AssignmentCrossover extends AbstractCrossover<Assignment> {
    public AssignmentCrossover() {
        this(1);
    }

    public AssignmentCrossover(int crossoverPoints) {
        super(crossoverPoints);
    }

    public AssignmentCrossover(int crossoverPoints, Probability crossoverProbability) {
        super(crossoverPoints, crossoverProbability);
    }

    public AssignmentCrossover(NumberGenerator<Integer> crossoverPointsVariable) {
        super(crossoverPointsVariable);
    }

    public AssignmentCrossover(NumberGenerator<Integer> crossoverPointsVariable, NumberGenerator<Probability> crossoverProbabilityVariable) {
        super(crossoverPointsVariable, crossoverProbabilityVariable);
    }

    protected List<Assignment> mate(Assignment a1, Assignment a2, int numberOfCrossoverPoints, Random rng) {
        /*if (parent1.length() != parent2.length()) {
            throw new IllegalArgumentException("Cannot perform cross-over with different length parents.");
        } else {
            StringBuilder offspring1 = new StringBuilder(parent1);
            StringBuilder offspring2 = new StringBuilder(parent2);

            for(int i = 0; i < numberOfCrossoverPoints; ++i) {
                int crossoverIndex = 1 + rng.nextInt(parent1.length() - 1);

                for(int j = 0; j < crossoverIndex; ++j) {
                    char temp = offspring1.charAt(j);
                    offspring1.setCharAt(j, offspring2.charAt(j));
                    offspring2.setCharAt(j, temp);
                }
            }

        }*/
        List<Assignment> result = new ArrayList(2);
        result.add(new Assignment(a1.getRoomNo(), a2.getReservationRoomNo()));
        result.add(new Assignment(a2.getRoomNo(), a1.getReservationRoomNo()));
        return result;
    }
}
