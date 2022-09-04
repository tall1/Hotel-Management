package com.hotels.entities.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskDTO {
    private long taskId;
    private int userId;
    private String date;
    private Integer elitism;
    private Integer populationSize;
    private double mutationProb;
    private int selectionStrategy;
    private double selecDouble;
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
