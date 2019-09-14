import animals.PigeonFlock;
import util.Canvas;
import util.Util;

import javax.swing.*;
import java.awt.*;

/**
 * @author Max Graf 01527246
 * @date 30.10.2018
 */
class Test {

    // INV: The simulation window's width and height
    private static final int WIDTH = 1300;
    private static final int HEIGHT = 700;

    public static void main(String[] args){

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        frame.setResizable(true);
        frame.setVisible(true);

        Util.setDimension(new Dimension(WIDTH, HEIGHT));
        util.Canvas canvas = new Canvas(new PigeonFlock(200, WIDTH, HEIGHT));
        //Canvas canvas = new Canvas(new animals.StarlingFlock(200, WIDTH, HEIGHT));

        JScrollPane jScrollPane = new JScrollPane(canvas);
        jScrollPane.getHorizontalScrollBar().addAdjustmentListener(
            e -> canvas.setTranslateX(e.getValue()));
        jScrollPane.getVerticalScrollBar().addAdjustmentListener(
            e -> canvas.setTranslateY(e.getValue()));
        jScrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        frame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
        frame.pack();

        while (true) {
            canvas.repaint();
        }
    }
}
