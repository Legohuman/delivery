package org.tirnak.binpacking.model;

import java.util.Collections;
import java.util.stream.Stream;

/**
 * Created by kirill on 12.03.16.
 */
public class Space extends Area {

    public Space(int x0, int y0, int xd, int yd) {
        this.x0 = x0;
        this.y0 = y0;
        this.xd = xd;
        this.yd = yd;
    }

    public Space(int xd, int yd) {
        this.xd = xd;
        this.yd = yd;
    }

    public int findMinSpace(Box box) {
        if (!fitAnyhow(box)) {
            throw new RuntimeException(box + "doesn't fit anyhow");
        }
        Integer[] spaces = Collections.nCopies(4, Integer.MAX_VALUE).toArray(new Integer[0]);
        if (fit(box)) {
            spaces[0] = xd - box.xd;
            spaces[1] = yd - box.yd;
        }
        if (fitRotated(box)) {
            spaces[2] = xd - box.yd;
            spaces[3] = yd - box.xd;
        }
        return Stream.of(spaces).min((a, b) -> a - b).get();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Space space = (Space) o;

        if (x0 != space.x0) return false;
        if (y0 != space.y0) return false;
        if (xd != space.xd) return false;
        return yd == space.yd;

    }

    @Override
    public int hashCode() {
        int result = x0;
        result = 31 * result + y0;
        result = 31 * result + xd;
        result = 31 * result + yd;
        return result;
    }
}
