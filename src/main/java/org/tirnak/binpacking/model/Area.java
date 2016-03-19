package org.tirnak.binpacking.model;

/**
 * Created by kirill on 13.03.16.
 */
abstract public class Area {
    public int xd;
    public int yd;
    public int zd;
    public int x0;
    public int y0;
    public int z0;

    public int getVolume() {
        return xd * yd * zd;
    }
    public int getArea() {
        return xd * yd * zd;
    }

    public boolean intersects(Area area) {
        return intersectsByX(area) && intersectsByY(area) && intersectsByZ(area);
    }

    private boolean intersectsByZ(Area area) {
        return !(
                this.z0 >= area.z0 + area.zd ||
                        this.z0 + this.zd <= area.z0);
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
        return fit(area) || fitRotated1(area) || fitRotated2(area);
    }

    public boolean fit(Area area) {
        if ((xd < area.xd) || (yd < area.yd) || (zd < area.zd)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean fitRotated1(Area area) {
        if ((zd < area.xd) || (xd < area.yd) || (yd < area.zd) ) {
            return false;
        } else {
            return true;
        }
    }

    public boolean fitRotated2(Area area) {
        if ((yd < area.xd) || (zd < area.yd) || (xd < area.zd) ) {
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
