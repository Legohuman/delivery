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
import java.util.stream.Stream;

public class Calculator {
    private static final Logger LOG = LogManager.getLogger(Calculator.class);
    public static Calculator _instance;
    public int spacex;
    public int spacey;

    public Calculator(int spacex, int spacey) {
        this.spacex = spacex;
        this.spacey = spacey;
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
            Space currentContainer = new Space(0, 0, spacex, spacey);
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
            box.setCoord(0, 0);
        }
        return result;
    }

    private boolean boxesDontFit(List<Box> boxes) {
        Space tempSpace = new Space(0, 0, spacex, spacey);
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
        int sx1 = space.xd - box.xd;
        int sy1 = space.yd - box.yd;
        int sx2 = space.xd - box.yd;
        int sy2 = space.yd - box.xd;
        int minc = Stream.of(sx1, sx2, sy1, sy2).filter(i -> i >= 0).min(Integer::compare).get();
        if ((minc == sx2 || minc == sy2) && space.fitRotated(box)) {
            box.rotate();
        }
        box.setCoord(space.x0, space.y0);
        int xdd = space.xd - box.xd;
        int ydd = space.yd - box.yd;
        if (xdd > ydd) {
            if (space.yd - box.yd > 0) {
                spaces.add(new Space(space.x0, space.y0 + box.yd, box.xd, space.yd - box.yd));
            }
            if (space.xd - box.xd > 0) {
                spaces.add(new Space(space.x0 + box.xd, space.y0, space.xd - box.xd, space.yd));
            }
        } else {
            if (space.yd - box.yd > 0) {
                spaces.add(new Space(space.x0, space.y0 + box.yd, space.xd, space.yd - box.yd));
            }
            if (space.xd - box.xd > 0) {
                spaces.add(new Space(space.x0 + box.xd, space.y0, space.xd - box.xd, box.yd));
            }
        }
        if (debug) {
            int areaExpected = space.getArea();
            int areaActual = spaces.stream().mapToInt(Area::getArea).sum() +
                    box.xd * box.yd;
            LOG.debug(() -> "expected: " + areaExpected + ", actual: " + areaActual);
        }
        return spaces;
    }

    public static Space findFittestSpace(List<Space> spaces, Box box) {
        spaces.stream().filter(s -> s.fitAnyhow(box));
        return spaces.stream().filter(s -> s.fitAnyhow(box))
                .min((a, b) -> a.findMinSpace(box) - b.findMinSpace(box))
                .get();
    }

}

