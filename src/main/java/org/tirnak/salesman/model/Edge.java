package org.tirnak.salesman.model;

/**
 * User: Legohuman
 * Date: 18/03/16
 */
public interface Edge extends Fittable {

    Vertex getForm();

    Vertex getTo();
}
