package com.firstlinesoftware.delivery.calc.route;

import com.firstlinesoftware.delivery.storage.map.Storage;
import org.tirnak.salesman.model.Edge;
import org.tirnak.salesman.model.Graph;
import org.tirnak.salesman.model.Vertex;

import java.util.Collections;
import java.util.List;

/**
 * User: Legohuman
 * Date: 19/03/16
 */
public class LogisticGraph implements Graph {

    private final Storage storage;

    public LogisticGraph(Storage storage) {
        this.storage = storage;
    }

    @Override
    public List<Edge> getEdges(Vertex v) {
        return Collections.emptyList();
    }
}
