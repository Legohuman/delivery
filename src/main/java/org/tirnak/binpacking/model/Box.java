package org.tirnak.binpacking.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kirill on 12.03.16.
 */
public class Box extends Area implements Cloneable {

    public final int id;
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

    public Box(int xd, int yd) {
        id = indexer.getAndIncrement();
        this.xd = xd;
        this.yd = yd;
    }

    public Box(int xd, int yd, int id) {
        this.id = id;
        this.xd = xd;
        this.yd = yd;
    }

    public boolean intersects(Box box) {
        return container == box.container && super.intersects(box);
    }

    public void rotate() {
        int temp = xd;
        xd = yd;
        yd = temp;
    }

    public void setCoord(int x, int y) {
        x0 = x;
        y0 = y;
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
}
