package org.tirnak.salesman.service;

import com.firstlinesoftware.delivery.dto.PaymentInfo;
import org.tirnak.binpacking.model.Box;
import org.tirnak.salesman.model.Edge;
import org.tirnak.salesman.model.Graph;
import org.tirnak.salesman.model.Vertex;

import java.util.List;
import java.util.Map;

/**
 * Created by kirill on 19.03.16.
 */
public interface GraphService {
    List<Edge> getRoute (Vertex from, Vertex to, Map<PaymentInfo.ContainerType, List<Box>> typeToBoxes, Graph graph);
}
