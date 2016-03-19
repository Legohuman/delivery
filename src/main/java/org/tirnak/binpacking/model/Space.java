package org.tirnak.binpacking.model;

/**
 * Created by kirill on 12.03.16.
 */
public class Space extends Area {


    public Space(int x0, int y0, int z0, int xd, int yd, int zd) {
        this.z0 = z0;
        this.x0 = x0;
        this.y0 = y0;
        this.zd = zd;
        this.xd = xd;
        this.yd = yd;
    }

    public Space(int xd, int yd, int zd) {
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
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
