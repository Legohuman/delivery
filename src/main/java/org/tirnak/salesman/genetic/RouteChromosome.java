package org.tirnak.salesman.genetic;

import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;
import org.tirnak.salesman.model.Edge;

import java.util.List;

/**
 * Created by kise0116 on 14.03.2016.
 */
public class RouteChromosome extends AbstractListChromosome<Edge> {

    /**
     * Constructor.
     *
     * @param representation inner representation of the chromosome
     * @throws InvalidRepresentationException iff the <code>representation</code> can not represent a valid chromosome
     */
    public RouteChromosome(final List<Edge> representation) throws InvalidRepresentationException {
        super(representation);
    }

    /**
     * Constructor.
     *
     * @param representation inner representation of the chromosome
     * @throws InvalidRepresentationException iff the <code>representation</code> can not represent a valid chromosome
     */
    public RouteChromosome(final Edge[] representation) throws InvalidRepresentationException {
        super(representation);
    }

    @Override
    protected void checkValidity(List<Edge> list) throws InvalidRepresentationException {
        if (!list.stream().allMatch(b -> b != null)) {
            throw new InvalidRepresentationException(LocalizedFormats.ARGUMENT_OUTSIDE_DOMAIN);
        }
    }

    @Override
    public AbstractListChromosome<Edge> newFixedLengthChromosome(List<Edge> list) {
        return new RouteChromosome(list);
    }

    /**
     * deeply copies representation
     */
    @Override
    public List<Edge> getRepresentation() {
        return super.getRepresentation();
    }

    /**
     * the more - the worse
     */
    @Override
    public double fitness() {
        return 1;
    }
}
