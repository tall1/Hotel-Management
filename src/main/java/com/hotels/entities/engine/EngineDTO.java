package com.hotels.entities.engine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EngineDTO {
    private long taskId;
    private int userId;
    private String date;
    private double mutationProb;
    private int selectionStrategy;
    private double selecdouble;
    private long maxDuration;
    private int generationCount;
    private int generationLimit;
    private double targetFitness;
    private int terminationElapsedTime;
    private int terminationGenerationCount;
    private int terminationStagnation;
    private int terminationTargetFitness;
    private int terminationUserAbort;
}
