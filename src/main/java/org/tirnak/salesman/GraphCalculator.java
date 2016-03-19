package org.tirnak.salesman;

import com.firstlinesoftware.delivery.dto.PaymentInfo;
import org.tirnak.binpacking.model.Box;
import org.tirnak.salesman.model.Edge;
import org.tirnak.salesman.model.Vertex;
import org.tirnak.salesman.service.GraphService;

import java.util.List;
import java.util.Map;

/**
 * Created by kirill on 19.03.16.
 */
public class GraphCalculator implements GraphService {

    @Override
    public List<Edge> getRoute(Vertex from, Vertex to, Map<PaymentInfo.ContainerType, List<Box>> typeToBoxes) {
        return null;
    }
}
