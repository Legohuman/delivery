package org.tirnak.salesman.genetic;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.GeneticAlgorithm;
import org.apache.commons.math3.genetics.MutationPolicy;
import org.apache.commons.math3.genetics.RandomKey;
import org.tirnak.salesman.model.Edge;

import java.util.List;

/**
 * Created by kirill on 15.03.16.
 */
public class SomeMutation implements MutationPolicy {
    /**
     * {@inheritDoc}
     *
     * @throws MathIllegalArgumentException if <code>original</code> is not a {@link RandomKey} instance
     */
    public Chromosome mutate(final Chromosome original) throws MathIllegalArgumentException {

        RouteChromosome originalBC = (RouteChromosome) original;
        // gets a copy
        List<Edge> repr = originalBC.getRepresentation();
        int rInd = GeneticAlgorithm.getRandomGenerator().nextInt(repr.size());
        Edge toMove = repr.remove(rInd);
        repr.add(0, toMove);

        return new RouteChromosome(repr);
    }

}
