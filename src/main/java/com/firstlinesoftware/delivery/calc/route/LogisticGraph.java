package com.firstlinesoftware.delivery.calc.route;

import com.firstlinesoftware.delivery.dto.PaymentInfo;
import com.firstlinesoftware.delivery.dto.SegmentInfo;
import com.firstlinesoftware.delivery.dto.SegmentVal;
import com.firstlinesoftware.delivery.storage.map.Storage;
import org.tirnak.salesman.model.Edge;
import org.tirnak.salesman.model.Graph;
import org.tirnak.salesman.model.Vertex;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Legohuman
 * Date: 19/03/16
 */
public class LogisticGraph implements Graph {

    private final Storage storage;
    private final PaymentInfo paymentInfo;

    public LogisticGraph(Storage storage, PaymentInfo paymentInfo) {
        this.storage = storage;
        this.paymentInfo = paymentInfo;
    }

    @Override
    public List<Edge> getEdges(Vertex v) {
        List<SegmentVal> segmentVals = storage.maps().segments().get(v.getCityCode());
        return segmentVals.stream().map(s -> {
            SegmentInfo si = new SegmentInfo();
            si.setFrom(storage.maps().cities().get(s.getFrom()));
            si.setTo(storage.maps().cities().get(s.getTo()));
            si.setTransportType(s.getTransportType());
            si.setCompany(s.getCompany());
            return new LogisticEdge(storage, paymentInfo, si);
        }).collect(Collectors.toList());
    }
}
