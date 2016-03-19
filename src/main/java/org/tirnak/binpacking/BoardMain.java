package org.tirnak.binpacking;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tirnak.binpacking.genetic.Optimizer;
import org.tirnak.binpacking.model.*;
import org.tirnak.binpacking.model.Box;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

/**
 * Created by kirill on 12.03.16.
 */
class BoardMain extends JComponent {

    private static final Logger LOG = LogManager.getLogger(BoardMain.class);
    private static final BoardMain myComponent = new BoardMain();
    static int spacexd = 150;
    static int spaceyd = 400;
    static int spacezd = 300;
    public java.util.List<org.tirnak.binpacking.model.Box> boxes;
    int currentContainer = 0;

    public static void main(String[] a) {
        genAlg();
    }

    private static void genAlg() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setBounds(20, 20, 1000, 1000);

        window.getContentPane().add(myComponent);
        window.setVisible(true);

        myComponent.boxes = new ArrayList<>();
        myComponent.boxes.add(Box.newBuilder().setXd(10).setYd(20).setZd(30).setWeight(15).build());
        myComponent.boxes.add(Box.newBuilder().setXd(10).setYd(20).setZd(20).setWeight(15).build());
        myComponent.boxes.add(Box.newBuilder().setXd(10).setYd(40).setZd(30).setWeight(15).build());
        myComponent.boxes.add(Box.newBuilder().setXd(10).setYd(20).setZd(30).setWeight(15).build());
        myComponent.boxes.add(Box.newBuilder().setXd(10).setYd(10).setZd(30).setWeight(15).build());
        myComponent.boxes.add(Box.newBuilder().setXd(10).setYd(20).setZd(80).setWeight(15).build());
        myComponent.boxes.add(Box.newBuilder().setXd(20).setYd(20).setZd(30).setWeight(15).build());
        myComponent.boxes.add(Box.newBuilder().setXd(15).setYd(20).setZd(30).setWeight(15).build());
        myComponent.boxes.add(Box.newBuilder().setXd(10).setYd(20).setZd(30).setWeight(65).build());
        myComponent.boxes.add(Box.newBuilder().setXd(10).setYd(30).setZd(30).setWeight(15).build());
//        Random random = new Random();
//        for (int i = 0; i < 75; i++) {
//            int x = (random.nextInt(10)+1) * 10;
//            int y = (random.nextInt(10)+1) * 10;
//            LOG.debug(() -> "x is " + x + ", y is " + y);
//            myComponent.boxes.add(new org.tirnak.binpacking.model.Box(x,y));
//        }

        Calculator._instance = new Calculator(spacexd, spaceyd, spacezd);
        myComponent.boxes = Optimizer.main(myComponent.boxes);
        int containers = Calculator._instance.calculate(myComponent.boxes);
        LOG.debug(() -> containers + " containers needed");
        int delay = 1000; //milliseconds

        TimerTask taskPerformer = new TimerTask() {
            @Override
            public void run() {
                if (myComponent.currentContainer > containers * 4) {
                    System.exit(0);
                }
                myComponent.repaint();
                LOG.debug(() -> "currentContainer " + myComponent.currentContainer);
            }
        };

        new java.util.Timer().schedule(taskPerformer, delay, delay);

    }

    private static void justDraw() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setBounds(20, 20, 1000, 1000);

        window.getContentPane().add(myComponent);
        window.setVisible(true);

        myComponent.boxes = new ArrayList<>();
//        myComponent.boxes.add(new org.tirnak.binpacking.model.Box(40,40));
//        myComponent.boxes.add(new org.tirnak.binpacking.model.Box(50,10));
//        myComponent.boxes.add(new org.tirnak.binpacking.model.Box(47,30));
//        myComponent.boxes.add(new org.tirnak.binpacking.model.Box(80,40));
//        myComponent.boxes.add(new org.tirnak.binpacking.model.Box(200,30));
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            int x = (random.nextInt(10) + 1) * 10;
            int y = (random.nextInt(10) + 1) * 10;
            int z = (random.nextInt(10) + 1) * 10;
            int weight = (random.nextInt(10) + 1) * 10;
            LOG.debug(() -> "x is " + x + ", y is " + y);
            myComponent.boxes.add(Box.newBuilder().setXd(x).setYd(y).setZd(z).setWeight(weight).build());
        }

        Calculator calculator = new Calculator(spacexd, spaceyd, spacezd);
        int containers = calculator.calculate(myComponent.boxes);
        LOG.debug(() -> containers + " containers needed");
        int delay = 1000; //milliseconds

        TimerTask taskPerformer = new TimerTask() {
            @Override
            public void run() {
                if (myComponent.currentContainer > containers * 4) {
                    System.exit(0);
                }
                myComponent.repaint();
                LOG.debug(() -> "currentContainer " + myComponent.currentContainer);
            }
        };

        new java.util.Timer().schedule(taskPerformer, delay, delay);
    }


    public void paint(Graphics g) {
        Random rand = new Random();
        Origin c = new Origin();

        g.drawRect(c.x, c.y, spacexd, spaceyd);
        for (org.tirnak.binpacking.model.Box box : boxes) {
            if ((box.container != currentContainer / 4)) {
                continue;
            }
            float red = rand.nextFloat();
            float green = rand.nextFloat();
            float blue = rand.nextFloat();
            Color randomColor = new Color(red, green, blue);
            g.setColor(randomColor);
            g.fillRect(box.x0 + c.x, box.y0 + c.y, box.xd, box.yd);
        }
        myComponent.currentContainer++;
    }


}
