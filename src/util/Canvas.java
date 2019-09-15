package util;

import animals.IBirdFlock;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.io.IOException;

/**
 * @author Max Graf 01527246
 * @date 30.10.2018
 */
public class Canvas extends JPanel {

    private IBirdFlock flock;

    private Image background;

    private float scale = 1;

    private float translateX = 0;
    private float translateY = 0;

    public Canvas(IBirdFlock flock){
        super(null);
        this.flock = flock;

        try {
            String BACKGROUNDPATH = "cloud_background.jpg";
            background = ImageIO.read(getClass().getResource(BACKGROUNDPATH));
        } catch (IOException e) {
            background = null;
            e.printStackTrace();
        }

        addMouseWheelListener(new MouseAdapter() {
            /**
             * PRE: e is not null
             * POST: scale is adjusted according to mouse wheel movement
             * @param e
             */
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double delta = 0.05f * e.getPreciseWheelRotation();
                scale += delta;
                if(scale < 1){
                    scale = 1;
                }
                Canvas.this.revalidate();
            }
        });
    }

    /**
     * PRE: g is not null
     * POST: All the updated game elements for one frame are drawn in 2D space
     * @param g a Graphics object
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        AffineTransform at = AffineTransform.getScaleInstance(scale, scale);

        g2d.drawImage(background, at,this);

        at.concatenate(AffineTransform.getTranslateInstance(-getTranslateX(), -getTranslateY()));

        flock.run(g2d, at);

        g2d.dispose();
    }

    /**
     * PRE: Preferred size has been set
     * POST: The preferred window size is returned
     * @return
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int) (Util.getDimension().width * scale), (int) (Util.getDimension().height * scale));
    }

    private float getTranslateX() {
        return translateX;
    }

    private float getTranslateY() {
        return translateY;
    }

    /**
     * POST: translateX is set to the specified float value
     * @param translateX
     */
    public void setTranslateX(float translateX) {
        this.translateX = translateX;
    }

    /**
     * POST: translateY is set to the specified float value
     * @param translateY
     */
    public void setTranslateY(float translateY) {
        this.translateY = translateY;
    }
}
