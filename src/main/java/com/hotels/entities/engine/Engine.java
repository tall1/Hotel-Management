package com.hotels.entities.engine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotels.entities.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "engine")
@Getter
@Setter
@NoArgsConstructor
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private long taskId;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    @Column(name = "status")
    private String status;
    @Column(name = "mutation_prob")
    private Double mutationProb;
    @Column(name = "selection_strategy")
    private Integer selectionStrategy;
    @Column(name = "selection_double")
    private Double selecDouble;
    @Column(name = "max_duration")
    private Long maxDuration;
    @Column(name = "generation_count")
    private Integer generationCount;
    @Column(name = "generation_limit")
    private Integer generationLimit;
    @Column(name = "natural_fitness")
    private Boolean naturalFitness;
    @Column(name = "target_fitness")
    private Double targetFitness;
    @Column(name = "term_elap_time")
    private Integer terminationElapsedTime;
    @Column(name = "term_gen_count")
    private Integer terminationGenerationCount;
    @Column(name = "term_stagnation")
    private Integer terminationStagnation;
    @Column(name = "term_target_fitness")
    private Integer terminationTargetFitness;
    @Column(name = "term_user_abort")
    private Integer terminationUserAbort;

    @JsonIgnore
    /* Returns: int[] with termination condition integers as described below:
     * TerminationCondition Integers:
     * 1. ElapsedTime
     * 2. GenerationCount
     * 3. Stagnation
     * 4. TargetFitness
     * 5. UserAbort
     * Think of this as a map from TerminationCondition to int.
     * */
    public int[] getTerminationInts() {
        int i = 0;
        int[] termConds = new int[5];
        if (this.terminationElapsedTime >= 1) {
            termConds[i++] = 1;
        }
        if (this.terminationGenerationCount >= 1) {
            termConds[i++] = 2;
        }
        if (this.terminationStagnation >= 1) {
            termConds[i++] = 3;
        }
        if (this.terminationTargetFitness >= 1) {
            termConds[i++] = 4;
        }
        if (this.terminationUserAbort >= 1) {
            termConds[i++] = 5;
        }
        return termConds;
    }

    @JsonIgnore
    public int getUserId() {
        return this.user.getId();
    }
}
