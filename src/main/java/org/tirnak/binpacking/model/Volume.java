package org.tirnak.binpacking.model;

/**
 * Created by kirill on 13.03.16.
 */
abstract public class Volume {
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

    public boolean intersects(Volume volume) {
        return intersectsByX(volume) && intersectsByY(volume) && intersectsByZ(volume);
    }

    private boolean intersectsByZ(Volume volume) {
        return !(
                this.z0 >= volume.z0 + volume.zd ||
                        this.z0 + this.zd <= volume.z0);
    }

    public boolean intersectsByY(Volume volume) {
        return !(
                this.y0 >= volume.y0 + volume.yd ||
                        this.y0 + this.yd <= volume.y0);
    }

    public boolean intersectsByX(Volume volume) {
        return !(
                this.x0 + this.xd <= volume.x0 ||
                        this.x0 >= volume.x0 + volume.xd);
    }

    public boolean fitAnyhow(Volume volume) {
        return fit(volume) || fitRotated1(volume) || fitRotated2(volume);
    }

    public boolean fit(Volume volume) {
        if ((xd < volume.xd) || (yd < volume.yd) || (zd < volume.zd)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean fitRotated1(Volume volume) {
        if ((zd < volume.xd) || (xd < volume.yd) || (yd < volume.zd)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean fitRotated2(Volume volume) {
        if ((yd < volume.xd) || (zd < volume.yd) || (xd < volume.zd)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean equalsByArea(Volume a) {
        return xd * yd == a.xd * a.yd;
    }

    public boolean equalsByDim(Volume a) {
        return xd == a.xd && yd == a.yd;
    }
}
