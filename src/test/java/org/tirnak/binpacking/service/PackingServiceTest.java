package org.tirnak.binpacking.service;

import org.junit.Test;
import org.tirnak.binpacking.Calculator;
import org.tirnak.binpacking.model.Box;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by kirill on 20.03.16.
 */
public class PackingServiceTest {


    @Test
    public void testPack() throws Exception {
        Calculator calc = new Calculator(10,10,100);
        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            boxes.add(Box.newBuilder().setXd(10).setYd(15).setZd(10).build());
        }
        calc.calculate(boxes);

        for (Box boxi : boxes) {
            for (Box boxj : boxes) {
                if (boxi.equals(boxj)) {
                    continue;
                }
                assertFalse(boxi.intersects(boxj));
            }
        }
    }
}