package org.tirnak.binpacking.service;

import org.tirnak.binpacking.model.Box;
import org.tirnak.binpacking.model.Container;

import java.util.List;

/**
 * User: Legohuman
 * Date: 19/03/16
 */
public interface PackingService {

    List<Box> pack(List<Box> boxes, Container container);
}
