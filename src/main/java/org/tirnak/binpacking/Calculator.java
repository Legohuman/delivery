package org.tirnak.binpacking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tirnak.binpacking.model.Box;
import org.tirnak.binpacking.model.Container;
import org.tirnak.binpacking.model.Volume;

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

        List<Container> containers = new ArrayList<>();
        int spaceIndex = 0;
        do {
            Container currentContainer = new Container(0, 0, 0, spacex, spacey, spacez);
            containers.add(currentContainer);

            List<Container> tempContainers = new LinkedList<>();
            tempContainers.add(currentContainer);

            while (anyBoxFit(tempContainers, localBoxesToPack)) {
                for (Box box : localBoxesToPack) {
                    try {
                        Container fittest = findFittestSpace(tempContainers, box);
                        tempContainers.remove(fittest);
                        List<Container> remainings = placeBox(fittest, box);
                        box.setContainer(spaceIndex);
                        localBoxesToPack.remove(box);
                        tempContainers.addAll(remainings);
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
        Container tempContainer = new Container(0, 0, 0, spacex, spacey, spacez);
        return !boxes.stream().allMatch(tempContainer::fitAnyhow);
    }

    public static boolean anyBoxFit(List<Container> containers, List<Box> boxes) {
        for (Container container : containers) {
            for (Box box : boxes) {
                if (box.alreadyPlaced()) {
                    continue;
                }
                if (container.fitAnyhow(box)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Container> placeBox(Container container, Box box) {
        List<Container> containers = new ArrayList<>();

        int minGap = box.getMinGapAndRotate(container);
        int gaps[] = new int[3];
        gaps[0] = container.xd - box.xd;
        gaps[1] = container.yd - box.yd;
        gaps[2] = container.zd - box.zd;
        if (container.xd - box.xd == gaps[2]) {
            containers.add(new Container(container.x0 + box.xd, container.y0, container.z0, container.xd - box.xd, container.yd, container.zd));
            if (container.zd - box.zd == gaps[0]) {
                containers.add(new Container(container.x0, container.y0, container.z0 + box.zd, box.xd, box.yd, container.zd - box.zd));
                containers.add(new Container(container.x0, container.y0 + box.yd, container.z0, container.xd - box.xd, container.yd - box.yd, container.zd));
            } else {
                containers.add(new Container(container.x0, container.y0 + box.yd, container.z0, box.xd, container.yd - box.yd, box.zd));
                containers.add(new Container(container.x0, container.y0, container.z0 + box.zd, box.xd, container.yd, container.zd - box.zd));
            }
        } else if (container.yd - box.yd == gaps[2]) {
            containers.add(new Container(container.x0, container.y0 + box.yd, container.z0, container.xd, container.yd, container.zd - box.xd));
            if (container.xd - box.xd == gaps[0]) {
                containers.add(new Container(container.x0 + box.xd, container.y0, container.z0, container.xd - box.xd, box.yd, box.zd));
                containers.add(new Container(container.x0, container.y0, container.z0 + box.zd, container.xd, box.yd, container.zd - box.zd));
            } else {
                containers.add(new Container(container.x0 + box.xd, container.y0, container.z0, container.xd - box.xd, box.yd, container.zd));
                containers.add(new Container(container.x0, container.y0, container.z0 + box.zd, box.xd, box.yd, container.zd - box.zd));
            }
        } else {
            containers.add(new Container(container.x0, container.y0, container.z0 + box.zd, container.xd, container.yd, container.zd - box.zd));
            if (container.xd - box.xd == gaps[0]) {
                containers.add(new Container(container.x0 + box.xd, container.y0, container.z0, container.xd - box.xd, box.yd, box.zd));
                containers.add(new Container(container.x0, container.y0 + box.yd, container.z0, container.xd, container.yd - box.yd, box.zd));
            } else {
                containers.add(new Container(container.x0 + box.xd, container.y0, container.z0, container.xd - box.xd, container.yd, box.zd));
                containers.add(new Container(container.x0, container.y0 + box.yd, container.z0, box.xd, container.yd - box.yd, box.zd));
            }
        }

        if (debug) {
            int volExpected = container.getVolume();
            int volActual = containers.stream().mapToInt(Volume::getVolume).sum() +
                    box.xd * box.yd;
            LOG.debug(() -> "expected: " + volExpected + ", actual: " + volActual);
        }
        return containers;
    }

    public static Container findFittestSpace(List<Container> containers, Box box) {
        containers.stream().filter(s -> s.fitAnyhow(box));
        return containers.stream().filter(s -> s.fitAnyhow(box))
                .min((a, b) -> box.getMinGap(a) -box.getMinGap(b))
                .get();
    }

}

