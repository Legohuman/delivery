package org.tirnak.binpacking.model;

/**
 * Created by kirill on 12.03.16.
 */
public class Container extends Volume {


    public Container(int x0, int y0, int z0, int xd, int yd, int zd) {
        this.z0 = z0;
        this.x0 = x0;
        this.y0 = y0;
        this.zd = zd;
        this.xd = xd;
        this.yd = yd;
    }

    public Container(int xd, int yd, int zd) {
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Container container = (Container) o;

        if (x0 != container.x0) return false;
        if (y0 != container.y0) return false;
        if (xd != container.xd) return false;
        return yd == container.yd;

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
