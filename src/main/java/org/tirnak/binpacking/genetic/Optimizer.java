package org.tirnak.binpacking.genetic;

import org.apache.commons.math3.genetics.*;
import org.tirnak.binpacking.Calculator;
import org.tirnak.binpacking.model.Box;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kise0116 on 14.03.2016.
 */
public class Optimizer {

    private static final int DIMENSION = 50;
    private static final int POPULATION_SIZE = 50;
    private static final int NUM_GENERATIONS = 500;
    private static final double ELITISM_RATE = 0.2;
    private static final double CROSSOVER_RATE = 1;
    private static final double MUTATION_RATE = 0.1;
    private static final int TOURNAMENT_ARITY = 5;

    public static List<Box> main(List<Box> boxes, Calculator calculator) {// initialize a new org.genetic algorithm
        GeneticAlgorithm ga = new GeneticAlgorithm(
                new OrderedCrossover<>(),
                CROSSOVER_RATE,
                new MoveToHeadMutation(),
                MUTATION_RATE,
                new TournamentSelection(TOURNAMENT_ARITY)
        );

        Population initial = getInitialPopulation(boxes, calculator);

        StoppingCondition stopCond = new FixedGenerationCount(NUM_GENERATIONS);

        Population finalPopulation = ga.evolve(initial, stopCond);

        return ((BoxChromosome) finalPopulation.getFittestChromosome()).getRepresentation();
    }

    private static Population getInitialPopulation(List<Box> boxes, Calculator calculator) {
        List<Chromosome> popList = new LinkedList<>();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            List<Box> newSequence = boxes.stream().map(Box::CloneNonApi).collect(Collectors.toList());
            Collections.shuffle(newSequence);
            popList.add(BoxChromosome.getNewInstance(newSequence, calculator));
        }
        return new ElitisticListPopulation(popList, popList.size(), ELITISM_RATE);
    }

}
