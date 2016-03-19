package org.tirnak.binpacking.genetic;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.*;
import org.tirnak.binpacking.model.Box;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kirill on 15.03.16.
 */
public class ReorderCrossover implements CrossoverPolicy {
    @Override
    public ChromosomePair crossover(Chromosome first, Chromosome second) throws MathIllegalArgumentException {
        List<Box> listParent1 = ((BoxChromosome) first).getRepresentation();
        List<Box> listParent2 = ((BoxChromosome) first).getRepresentation();
        int size = listParent1.size();
        int[] points = new int[2];
        points[0] = GeneticAlgorithm.getRandomGenerator().nextInt(size);
        points[1] = GeneticAlgorithm.getRandomGenerator().nextInt(size);
        if (points[0] > points[1]) {
            Arrays.sort(points);
        }
        List<Box> listChild1 = new ArrayList<>();
        List<Box> listChild2 = new ArrayList<>();
        List<Box> temp1 = new ArrayList<>(listParent1);
        List<Box> temp2 = new ArrayList<>(listParent2);
        for (int i = 0; i < size; i++) {
            if (i >= points[0] && i <= points[1]) {
                final int iL = i;
                temp1.removeIf(b -> listParent2.get(iL).equalsByDim(b));
                temp2.removeIf(b -> listParent1.get(iL).equalsByDim(b));
            }
        }

        for (int i = 0; i < size; i++) {
            if (i >= points[0] && i <= points[1]) {
                listChild1.add(listParent1.get(i));
                listChild2.add(listParent2.get(i));
            } else {
                listChild1.add(temp2.remove(0));
                listChild2.add(temp1.remove(0));
            }
        }
        return new ChromosomePair(
                ((AbstractListChromosome) first).newFixedLengthChromosome(listChild1),
                ((AbstractListChromosome) second).newFixedLengthChromosome(listChild2));
    }
}
