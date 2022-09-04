package com.hotels.entities.task;

import com.hotels.utils.MyConstants;
import lombok.Getter;
import lombok.Setter;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.SelectionStrategy;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.selection.*;
import org.uncommons.watchmaker.framework.termination.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class TaskProperties {
    private long taskId;
    private int elitism;
    private int popSize;
    private Probability mutationProb;
    private SelectionStrategy<Object> selectionStrategy;
    private List<TerminationCondition> termCond;

    public TaskProperties() {
        taskId = MyConstants.EMPTY_TASK_ID;
        elitism = 5;
        popSize = 50;
        mutationProb = new Probability(0.2);
        this.selectionStrategy = getSelectionStrategy(2, 0.51);
        int[] termConds = {1};
        this.termCond = getTerminationConditions(
                termConds,
                5000L,
                1000,
                1000,
                1000.0
        );
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
                    termList.add(new Stagnation(generationLimit, MyConstants.NATURAL_FITNESS));
                    break;
                case 4:
                    termList.add(new TargetFitness(targetFitness, MyConstants.NATURAL_FITNESS));
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
}
