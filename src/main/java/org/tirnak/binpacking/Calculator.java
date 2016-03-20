package org.tirnak.binpacking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tirnak.binpacking.genetic.Optimizer;
import org.tirnak.binpacking.model.Box;
import org.tirnak.binpacking.model.Container;
import org.tirnak.binpacking.model.Volume;
import org.tirnak.binpacking.service.PackingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Calculator implements PackingService {
    private static final Logger LOG = LogManager.getLogger(Calculator.class);
    public static Calculator _instance;
    public int spacex;
    public int spacey;
    public int spacez;

    public Calculator() {}

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
                        for (int i = 0; i < tempContainers.size(); i++) {
                            Container ci = tempContainers.get(i);
                            for (int i1 = i; i1 < tempContainers.size(); i1++) {
                                Container cj = tempContainers.get(i1);
                                if (ci.equals(cj)) {
                                    continue;
                                }
                                if (ci.intersects(cj)) {
                                    LOG.debug(() -> "achtung! temp container " + ci + " intersects with " + cj);
                                }
                            }
                        }
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

        int justForRotation = box.getMinGapAndRotate(container);
        box.setCoord(container.x0, container.y0, container.z0);
        int gaps[] = new int[3];
        gaps[0] = container.xd - box.xd;
        gaps[1] = container.yd - box.yd;
        gaps[2] = container.zd - box.zd;
        Arrays.sort(gaps);
        if (container.xd - box.xd == gaps[2]) {
            containers.add(new Container(container.x0 + box.xd, container.y0, container.z0, container.xd - box.xd, container.yd, container.zd));
            if (container.zd - box.zd == gaps[0]) {
                if (container.zd - box.zd > 0) { containers.add(
                        new Container(container.x0, container.y0, container.z0 + box.zd, box.xd, box.yd, container.zd - box.zd));}
                if (container.xd - box.xd > 0 && container.yd - box.yd > 0) {containers.add(
                        new Container(container.x0, container.y0 + box.yd, container.z0, box.xd, container.yd - box.yd, container.zd));}
            } else {
                if (container.yd - box.yd > 0) {containers.add(
                        new Container(container.x0, container.y0 + box.yd, container.z0, box.xd, container.yd - box.yd, box.zd));}
                if (container.zd - box.zd > 0) {containers.add(
                        new Container(container.x0, container.y0, container.z0 + box.zd, box.xd, container.yd, container.zd - box.zd));}
            }
        } else if (container.yd - box.yd == gaps[2]) {
            containers.add(new Container(container.x0, container.y0 + box.yd, container.z0, container.xd, container.yd - box.yd, container.zd));
            if (container.xd - box.xd == gaps[0]) {
                if (container.xd - box.xd > 0) {containers.add(
                        new Container(container.x0 + box.xd, container.y0, container.z0, container.xd - box.xd, box.yd, box.zd));}
                if (container.zd - box.zd > 0) {containers.add(
                        new Container(container.x0, container.y0, container.z0 + box.zd, container.xd, box.yd, container.zd - box.zd));}
            } else {
                if (container.xd - box.xd > 0) {containers.add(
                        new Container(container.x0 + box.xd, container.y0, container.z0, container.xd - box.xd, box.yd, container.zd));}
                if (container.zd - box.zd > 0) {containers.add(
                        new Container(container.x0, container.y0, container.z0 + box.zd, box.xd, box.yd, container.zd - box.zd));}
            }
        } else {
            containers.add(new Container(container.x0, container.y0, container.z0 + box.zd, container.xd, container.yd, container.zd - box.zd));
            if (container.xd - box.xd == gaps[0]) {
                if (container.xd - box.xd > 0) {containers.add(
                    new Container(container.x0 + box.xd, container.y0, container.z0, container.xd - box.xd, box.yd, box.zd));}
                if (container.yd - box.yd > 0) {containers.add(
                    new Container(container.x0, container.y0 + box.yd, container.z0, container.xd, container.yd - box.yd, box.zd));}
            } else {
                if (container.xd - box.xd > 0) {containers.add(
                    new Container(container.x0 + box.xd, container.y0, container.z0, container.xd - box.xd, container.yd, box.zd));}
                if (container.yd - box.yd > 0) {containers.add(
                    new Container(container.x0, container.y0 + box.yd, container.z0, box.xd, container.yd - box.yd, box.zd));}
            }
        }

        if (debug) {
            int volExpected = container.getVolume();
            int volActual = containers.stream().mapToInt(Volume::getVolume).sum() +
                    box.xd * box.yd * box.zd;
            if (volActual != volExpected) {LOG.debug(() -> "expected: " + volExpected + ", actual: " + volActual);}
            if (!container.fit(box)) {
                LOG.debug(() -> container + " doesn't fit " + box);}
            if (containers.get(0) != null && !container.fit(containers.get(0))) {
                LOG.debug(() -> container + " doesn't fit " + containers.get(0));}
            if (containers.size() > 1 && !container.fit(containers.get(1))) {
                LOG.debug(() -> container + " doesn't fit " + containers.get(1));}
            if (containers.size() == 2 && !container.fit(containers.get(2))) {
                LOG.debug(() -> container + " doesn't fit " + containers.get(2));}
        }
        return containers;
    }

    public static Container findFittestSpace(List<Container> containers, Box box) {
        containers.stream().filter(s -> s.fitAnyhow(box));
        return containers.stream().filter(s -> s.fitAnyhow(box))
                .min((a, b) -> box.getMinGap(a) - box.getMinGap(b))
                .get();
    }

    @Override
    public List<Box> pack(List<Box> boxes, Container container) {
        this.spacex = container.xd;
        this.spacey = container.yd;
        this.spacez = container.zd;
        List<Box> optimalSeq = Optimizer.main(boxes, this);
        this.calculate(optimalSeq);
        return optimalSeq;
    }
}

