package org.tirnak.binpacking.model;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Created by kirill on 12.03.16.
 */
public class Box extends Area implements Cloneable {

    private int id;
    private static AtomicInteger indexer = new AtomicInteger();

    public int container = -1;

    public void setContainer(int container) {
        this.container = container;
    }

    public boolean alreadyPlaced() {
        return container != -1;
    }

    public boolean isUnplaced() {
        return container == -1;
    }

    public Box() {

    }

    public Box(int xd, int yd, int zd) {
        this.xd = xd;
        this.yd = yd;
    }

    public Box(int xd, int yd, int zd, int id) {
        this.id = id;
        this.xd = xd;
        this.yd = yd;
    }

    public boolean intersects(Box box) {
        return container == box.container && super.intersects(box);
    }

    public void rotate() {
        int temp = yd;
        yd = xd;
        xd = zd;
        zd = temp;
    }

    public void setCoord(int x, int y, int z) {
        x0 = x;
        y0 = y;
        z0 = z;
    }

    @Override
    public String toString() {
        return "org.model.Box{" +
                "xd=" + xd +
                ", yd=" + yd +
                ", x0=" + x0 +
                ", y0=" + y0 +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Box CloneNonApi() {
        try {
            return (Box) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getMinGapAndRotate(Area area) {
        int xx = area.xd - xd;
        int yy = area.yd - yd;
        int zz = area.zd - zd;
        int xz = area.xd - zd;
        int yx = area.yd - xd;
        int zy = area.zd - yd;
        int xy = area.xd - yd;
        int yz = area.yd - zd;
        int zx = area.zd - xd;
        try {
            int minGap = Stream.of(xx,yy,zz,xz,yx,zy,xy,yz,zx).filter(i -> i >= 0).min(Integer::compare).get();
            if (minGap == xx || minGap == yy || minGap == zz) {
                // don't rotate
            } else if (minGap == xz || minGap == yx || minGap == zy) {
                rotate();
            } else {
                rotate();rotate();
            }
            return minGap;
        } catch (NoSuchElementException e) {
            return -1;
        }
    }


    public int getMinGap(Area area) {
        int xx = area.xd - xd;
        int yy = area.yd - yd;
        int zz = area.zd - zd;
        int xz = area.xd - zd;
        int yx = area.yd - xd;
        int zy = area.zd - yd;
        int xy = area.xd - yd;
        int yz = area.yd - zd;
        int zx = area.zd - xd;
        try {
            int minGap = Stream.of(xx,yy,zz,xz,yx,zy,xy,yz,zx).filter(i -> i >= 0).min(Integer::compare).get();
            return minGap;
        } catch (NoSuchElementException e) {
            return -1;
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Box box = (Box) o;

        return id == box.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    public static Builder newBuilder() {
        return new Box().new Builder();
    }

    public class Builder {
        private Builder() {
            // private constructor
        }

        public Builder setXd (int xd) {Box.this.xd = xd;return this;}
        public Builder setYd (int yd) {Box.this.yd = yd;return this;}
        public Builder setZd (int zd) {Box.this.zd = zd;return this;}
        public Builder setWeight (int zd) {Box.this.zd = zd;return this;}

        public Box build() {
            Box.this.id = indexer.getAndIncrement();
            return Box.this;
        }

    }
}
