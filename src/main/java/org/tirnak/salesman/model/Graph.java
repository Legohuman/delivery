package org.tirnak.salesman.model;

import java.util.List;

/**
 * User: Legohuman
 * Date: 19/03/16
 */
public interface Graph {
    List<Edge> getEdges(Vertex v);
}
