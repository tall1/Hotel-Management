package com.hotels.service.utils;

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
public class EngineProperties {
    private Probability mutationProb;
    private SelectionStrategy<Object> selectionStrategy;
    private List<TerminationCondition> termCond;

    // Can I send Objects from client side?
    // How do I tell the Controller which SelectionStrategy I meant?


    public EngineProperties() {
        mutationProb = new Probability(0.2);
        this.selectionStrategy = getSelectionStrategy(2, 0.51);
        Integer[] termConds = {1,3,5};
        this.termCond = getTerminationConditions(
                termConds,
                5000L,
                1000,
                1000,
                true,
                1000.0
        );
    }

    public EngineProperties(Double prob,
                            Integer selectionStrategyInteger, Double selecDouble,
                            Long maxDuration, // ElapsedTime
                            Integer generationCount, // GenerationCount
                            Integer generationLimit,
                            Boolean naturalFitness, // Stagnation
                            Double targetFitness, // TargetFitness
                            Integer... termConds) {
        mutationProb = new Probability(prob);
        this.selectionStrategy = getSelectionStrategy(selectionStrategyInteger, selecDouble);
        this.termCond = getTerminationConditions(
                termConds,
                maxDuration,
                generationCount,
                generationLimit,
                naturalFitness,
                targetFitness
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
                return new TournamentSelection(new Probability(selecDouble));
            case 6:
                return new TruncationSelection(selecDouble);
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
            Integer[] termConds,
            Long maxDuration, // ElapsedTime
            Integer generationCount, // GenerationCount
            Integer generationLimit, Boolean naturalFitness, // Stagnation
            Double targetFitness // TargetFitness
    ) {
        List<TerminationCondition> termList = new ArrayList<>();
        for (Integer i : termConds) {
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
}
