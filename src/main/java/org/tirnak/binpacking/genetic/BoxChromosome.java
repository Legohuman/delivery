package org.tirnak.binpacking.genetic;

import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;
import org.tirnak.binpacking.Calculator;
import org.tirnak.binpacking.model.Box;
import org.tirnak.binpacking.model.Container;
import org.tirnak.binpacking.service.PackingService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kise0116 on 14.03.2016.
 */
public class BoxChromosome extends AbstractListChromosome<Box> {

    public Calculator getCalculator() {
        return calculator;
    }

    private Calculator calculator;

    public static BoxChromosome getNewInstance(List<Box> boxes, Calculator calculator) {
        BoxChromosome toReturn = new BoxChromosome(boxes);
        toReturn.setCalculator(calculator);
        return toReturn;
    }

    private void setCalculator(Calculator calculator) {
        this.calculator = calculator;
    }



    /**
     * Constructor.
     *
     * @param representation inner representation of the chromosome
     * @throws InvalidRepresentationException iff the <code>representation</code> can not represent a valid chromosome
     */
    public BoxChromosome(final List<Box> representation) throws InvalidRepresentationException {
        super(representation);
    }

    /**
     * Constructor.
     *
     * @param representation inner representation of the chromosome
     * @throws InvalidRepresentationException iff the <code>representation</code> can not represent a valid chromosome
     */
    public BoxChromosome(final Box[] representation) throws InvalidRepresentationException {
        super(representation);
    }

    @Override
    protected void checkValidity(List<Box> list) throws InvalidRepresentationException {
        if (!list.stream().allMatch(b -> b != null)) {
            throw new InvalidRepresentationException(LocalizedFormats.ARGUMENT_OUTSIDE_DOMAIN);
        }
    }

    @Override
    public AbstractListChromosome<Box> newFixedLengthChromosome(List<Box> list) {
        return BoxChromosome.getNewInstance(list, calculator);
    }

    /**
     * deeply copies representation
     */
    @Override
    public List<Box> getRepresentation() {
        return super.getRepresentation()
                .stream().map(Box::CloneNonApi)
                .collect(Collectors.toList());
    }

    /**
     * the more - the worse
     */
    @Override
    public double fitness() {
        return 1 / calculator.calculateAndUnset(getRepresentation());
    }

}
