package com.hotels.entities.task.status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotels.entities.task.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "status")
@Getter
@Setter
@NoArgsConstructor
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long statusId;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "status", fetch = FetchType.LAZY)
    @JsonIgnore
    private Task task;

    @Column(name = "status_str")
    private String statusStr;

    @Column(name = "best_fitness")
    private Double bestFitness;

    @Column(name = "current_generation")
    private int curGeneration;

    @Column(name = "elapsed_time")
    private long elapsedTime;
}
