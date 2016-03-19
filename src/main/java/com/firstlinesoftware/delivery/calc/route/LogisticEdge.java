package com.firstlinesoftware.delivery.calc.route;

import com.firstlinesoftware.delivery.calc.ImportProductCalc;
import com.firstlinesoftware.delivery.calc.TransportCalc;
import com.firstlinesoftware.delivery.calc.route.api.Edge;
import com.firstlinesoftware.delivery.calc.route.api.Fittable;
import com.firstlinesoftware.delivery.calc.route.api.Vertex;
import com.firstlinesoftware.delivery.dto.PaymentInfo;
import com.firstlinesoftware.delivery.dto.ProductInfo;
import com.firstlinesoftware.delivery.dto.SegmentInfo;
import com.firstlinesoftware.delivery.storage.map.Storage;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: Legohuman
 * Date: 18/03/16
 */
public class LogisticEdge implements Edge, Fittable {

    @NotNull
    private final Storage storage;

    @NotNull
    private final PaymentInfo paymentInfo;

    @NotNull
    private final SegmentInfo segmentInfo;

    public LogisticEdge(@NotNull Storage storage, @NotNull PaymentInfo paymentInfo, @NotNull SegmentInfo segmentInfo) {
        this.storage = storage;
        this.paymentInfo = paymentInfo;
        this.segmentInfo = segmentInfo;
    }

    @Override
    public Vertex getForm() {
        return segmentInfo.getFrom();
    }

    @Override
    public Vertex getTo() {
        return segmentInfo.getTo();
    }

    @Override
    public double fitness() {
        if (PaymentInfo.FitnessProperty.money.equals(paymentInfo.getFitnessProperty())) {
            return moneyFitness();
        } else {
            return timeFitness();
        }
    }

    private double moneyFitness() {
        List<ProductInfo> products = paymentInfo.getProducts();
        Validate.notNull(products, "Can not calculate fitness for null product list");
        String originCountry = paymentInfo.getOrigin().getCountry();
        String destinationCountry = paymentInfo.getDestination().getCountry();
        String fromCountry = segmentInfo.getFrom().getCountry();
        String toCountry = segmentInfo.getTo().getCountry();
        double result = 0;

        if (!fromCountry.equals(originCountry) && !fromCountry.equals(destinationCountry) ||
                !toCountry.equals(originCountry) && !toCountry.equals(destinationCountry)) {
            result += products.stream().mapToDouble(ProductInfo::getCost).sum();
        }
        result += new TransportCalc(storage).calculate(null, paymentInfo, segmentInfo);

        if (!originCountry.equals(destinationCountry) && toCountry.equals(destinationCountry)) {
            result += products.stream().mapToDouble(pi -> new ImportProductCalc(storage).calculate(pi, paymentInfo, segmentInfo)).sum();
        }
        return result;
    }

    private double timeFitness() {
        return 1;
    }
}
