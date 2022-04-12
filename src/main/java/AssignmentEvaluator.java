import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;

public class AssignmentEvaluator implements FitnessEvaluator<Assignment>
{
    private final String targetString = "HELLO WORLD";

    /**
     * Assigns one "fitness point" for every character in the
     * candidate String that matches the corresponding position in
     * the target string.
     */
    public double getFitness(Assignment candidate,
                             List<? extends Assignment> population)
    {
//        int matches = 0;
//        for (int i = 0; i < candidate.length(); i++)
//        {
//            if (candidate.charAt(i) == targetString.charAt(i))
//            {
//                ++matches;
//            }
//        }
        return Math.abs(candidate.getRoomNo() - candidate.getReservationRoomNo());
    }

    public boolean isNatural()
    {
        return false;
    }
}