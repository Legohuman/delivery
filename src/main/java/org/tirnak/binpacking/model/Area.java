package org.tirnak.binpacking.model;

/**
 * Created by kirill on 13.03.16.
 */
abstract public class Area {
    public int xd;
    public int yd;
    public int x0;
    public int y0;

    public int getArea() {
        return xd * yd;
    }

    public boolean intersects(Area area) {
        return intersectsByX(area) && intersectsByY(area);
    }

    public boolean intersectsByY(Area area) {
        return !(
                this.y0 >= area.y0 + area.yd ||
                        this.y0 + this.yd <= area.y0);
    }

    public boolean intersectsByX(Area area) {
        return !(
                this.x0 + this.xd <= area.x0 ||
                        this.x0 >= area.x0 + area.xd);
    }

    public boolean fitAnyhow(Area area) {
        return fit(area) || fitRotated(area);
    }

    public boolean fit(Area area) {
        if ((xd < area.xd) || (yd < area.yd)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean fitRotated(Area area) {
        if ((xd < area.yd) || (yd < area.xd)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean equalsByArea(Area a) {
        return xd * yd == a.xd * a.yd;
    }

    public boolean equalsByDim(Area a) {
        return xd == a.xd && yd == a.yd;
    }
}
