package animals;

import util.ITimeBased;
import util.Util;
import util.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
/**
 * @author David Scherer 11777743
 */
public class Nest extends Bird implements ITimeBased {

    private boolean isNest, isInvader;
    private Timer timer;

    /**
     * POST: new nest is created, that respawns after a certain time
     * @param id
     * @param x
     * @param y
     * @param z
     * @param isNest
     * @param isInvader
     */
    Nest(int id, float x, float y, float z, boolean isNest, boolean isInvader){
        super(id, x, y, z, isNest, isInvader);
        setVelocity(new Vector());
        this.isNest = isNest;
        this.isInvader = isInvader;

        // Reset position of invader every 10 seconds
        timer = new Timer(getDelay(), evt -> {
            if (timer.getDelay() == getDelay()) {
                setPosition(new Vector(Util.randomInRange(0, Util.getDimension().width),
                    Util.randomInRange(0, Util.getDimension().height),
                    Util.randomInRange(30, 60)));
                timer.restart();
            }
        });
        timer.start();
    }

    @Override
    public void draw(Graphics2D g, AffineTransform at){
        g.setTransform(at);
        g.setColor(Color.green);
        g.fillRect((int) getPosition().getX(), (int) getPosition().getY(), 20, 20);
    }

    /**
     * PRE: g, birds and at are not null
     * POST: The nests position will not be updated, just the view updated and drawn
     * @param g a Graphics2D object
     * @param birds a list of not null bird objects
     * @param at an AffineTransform object
     */
    @Override
    public void run(Graphics2D g, List<IBird> birds, AffineTransform at) {
        view(birds);
        draw(g, at);
    }

    public boolean isNest() {
        return isNest;
    }
    public boolean isInvader(){
        return isInvader;
    }

    @Override
    public int getDelay() {
        return 5000;
    }
}

