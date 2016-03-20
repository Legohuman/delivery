package org.tirnak.salesman;

import com.firstlinesoftware.delivery.dto.PaymentInfo;
import org.tirnak.binpacking.model.Box;
import org.tirnak.salesman.model.Edge;
import org.tirnak.salesman.model.Graph;
import org.tirnak.salesman.model.Vertex;
import org.tirnak.salesman.service.GraphService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kirill on 19.03.16.
 */
public class GraphCalculatorStub implements GraphService {

    private List<List<Edge>> variants;
    private Vertex to;

    @Override
    public List<Edge> getRoute(Vertex from, Vertex to, Map<PaymentInfo.ContainerType, List<Box>> typeToBoxes, Graph graph) {
//        while ()
        Map<Vertex, Boolean> vertxMap = new HashMap<>();
        Vertex current = from;
        this.to = to;
        vertxMap.put(current, true);
        for (Edge edge : graph.getEdges(current)) {
            List<Edge> tmpVar = new ArrayList<>();
            tmpVar.add(edge);
            iterate(tmpVar, edge.getTo(), new HashMap<>(vertxMap), graph);
        }
        return null;
    }

    private void iterate (List<Edge> variant, Vertex newCurrent, Map<Vertex, Boolean> marked, Graph graph) {
        if (newCurrent.equals(to)) {
            variants.add(variant);
            return;
        }
        marked.put(newCurrent, true);
        for (Edge edge : graph.getEdges(newCurrent)) {
            if (edge.getTo().equals(newCurrent)) {
                continue;
            }
            if (marked.get(edge.getTo())) {
                continue;
            }
            List<Edge> tmpList = new ArrayList<>(variant);
            tmpList.add(edge);
            iterate(tmpList, edge.getTo(), new HashMap<>(marked), graph);
        }
    }




}
