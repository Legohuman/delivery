package org.tirnak.salesman.model;


/**
 * Created by kise0116 on 15.03.2016.
 */
public class Edge {
    private Vertex[] vertices;

    public Vertex getOther(Vertex one) {
        return vertices[0].equals(one) ? vertices[1] : vertices[0];
    }

}
