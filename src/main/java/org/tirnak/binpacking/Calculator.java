package org.tirnak.binpacking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tirnak.binpacking.model.Area;
import org.tirnak.binpacking.model.Box;
import org.tirnak.binpacking.model.Space;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Calculator {
    private static final Logger LOG = LogManager.getLogger(Calculator.class);
    public static Calculator _instance;
    public int spacex;
    public int spacey;
    public int spacez;

    public Calculator(int spacex, int spacey, int spacez) {
        this.spacex = spacex;
        this.spacey = spacey;
        this.spacez = spacez;
    }

    private static boolean debug = true;

    public int calculate(List<Box> boxesToPack) {

        List<Box> localBoxesToPack = new CopyOnWriteArrayList<>(boxesToPack);

        if (boxesDontFit(localBoxesToPack)) {
            throw new RuntimeException("some of the boxes don't fit");
        }

        List<Space> containers = new ArrayList<>();
        int spaceIndex = 0;
        do {
            Space currentContainer = new Space(0, 0, 0, spacex, spacey, spacez);
            containers.add(currentContainer);

            List<Space> tempSpaces = new LinkedList<>();
            tempSpaces.add(currentContainer);

            while (anyBoxFit(tempSpaces, localBoxesToPack)) {
                for (Box box : localBoxesToPack) {
                    try {
                        Space fittest = findFittestSpace(tempSpaces, box);
                        tempSpaces.remove(fittest);
                        List<Space> remainings = placeBox(fittest, box);
                        box.setContainer(spaceIndex);
                        localBoxesToPack.remove(box);
                        tempSpaces.addAll(remainings);
                    } catch (RuntimeException e) {
                        continue;
                    }
                }
            }

            spaceIndex++;
        } while (localBoxesToPack.size() > 0);

        if (debug) {
            for (Box boxi : boxesToPack) {
                for (Box boxj : boxesToPack) {
                    if (boxi.equals(boxj)) {
                        continue;
                    }
                    if (boxi.intersects(boxj)) {
                        LOG.error(() -> "Achtung! " + boxi + " intersects with " + boxj);
                    }
                }
            }
        }

        return containers.size();
    }

    public int calculateAndUnset(List<Box> boxesToPack) {
        int result = calculate(boxesToPack);
        for (Box box : boxesToPack) {
            box.setContainer(-1);
            box.setCoord(0, 0, 0);
        }
        return result;
    }

    private boolean boxesDontFit(List<Box> boxes) {
        Space tempSpace = new Space(0, 0, 0, spacex, spacey, spacez);
        return !boxes.stream().allMatch(tempSpace::fitAnyhow);
    }

    public static boolean anyBoxFit(List<Space> spaces, List<Box> boxes) {
        for (Space space : spaces) {
            for (Box box : boxes) {
                if (box.alreadyPlaced()) {
                    continue;
                }
                if (space.fitAnyhow(box)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Space> placeBox(Space space, Box box) {
        List<Space> spaces = new ArrayList<>();

        int minGap = box.getMinGapAndRotate(space);
        int gaps[] = new int[3];
        gaps[0] = space.xd - box.xd;
        gaps[1] = space.yd - box.yd;
        gaps[2] = space.zd - box.zd;
        if (space.xd - box.xd == gaps[2]) {
            spaces.add(new Space(space.x0 + box.xd, space.y0, space.z0, space.xd - box.xd, space.yd, space.zd));
            if (space.zd - box.zd == gaps[0]) {
                spaces.add(new Space(space.x0, space.y0, space.z0 + box.zd, box.xd, box.yd, space.zd - box.zd));
                spaces.add(new Space(space.x0, space.y0 + box.yd, space.z0, space.xd - box.xd, space.yd - box.yd, space.zd));
            } else {
                spaces.add(new Space(space.x0, space.y0 + box.yd, space.z0, box.xd, space.yd - box.yd, box.zd));
                spaces.add(new Space(space.x0, space.y0, space.z0 + box.zd, box.xd, space.yd, space.zd - box.zd));
            }
        } else if (space.yd - box.yd == gaps[2]) {
            spaces.add(new Space(space.x0, space.y0 + box.yd, space.z0, space.xd, space.yd, space.zd - box.xd));
            if (space.xd - box.xd == gaps[0]) {
                spaces.add(new Space(space.x0 + box.xd, space.y0, space.z0, space.xd - box.xd, box.yd, box.zd));
                spaces.add(new Space(space.x0, space.y0, space.z0 + box.zd, space.xd, box.yd, space.zd - box.zd));
            } else {
                spaces.add(new Space(space.x0 + box.xd, space.y0, space.z0, space.xd - box.xd, box.yd, space.zd));
                spaces.add(new Space(space.x0, space.y0, space.z0 + box.zd, box.xd, box.yd, space.zd - box.zd));
            }
        } else {
            spaces.add(new Space(space.x0, space.y0, space.z0 + box.zd, space.xd, space.yd, space.zd - box.zd));
            if (space.xd - box.xd == gaps[0]) {
                spaces.add(new Space(space.x0 + box.xd, space.y0, space.z0, space.xd - box.xd, box.yd, box.zd));
                spaces.add(new Space(space.x0, space.y0 + box.yd, space.z0, space.xd, space.yd - box.yd, box.zd));
            } else {
                spaces.add(new Space(space.x0 + box.xd, space.y0, space.z0, space.xd - box.xd, space.yd, box.zd));
                spaces.add(new Space(space.x0, space.y0 + box.yd, space.z0, box.xd, space.yd - box.yd, box.zd));
            }
        }

        if (debug) {
            int volExpected = space.getVolume();
            int volActual = spaces.stream().mapToInt(Area::getVolume).sum() +
                    box.xd * box.yd;
            LOG.debug(() -> "expected: " + volExpected + ", actual: " + volActual);
        }
        return spaces;
    }

    public static Space findFittestSpace(List<Space> spaces, Box box) {
        spaces.stream().filter(s -> s.fitAnyhow(box));
        return spaces.stream().filter(s -> s.fitAnyhow(box))
                .min((a, b) -> box.getMinGap(a) -box.getMinGap(b))
                .get();
    }

}

