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
public class Invader extends Bird implements ITimeBased {

    private boolean isNest, isInvader;
    private Timer timer;

    /**
     * POST: Creates a new animals.Invader that respawns after a certain time
     * @param id
     * @param x
     * @param y
     * @param z
     * @param isNest
     * @param isInvader
     */
    Invader(int id, float x, float y, float z,  boolean isNest, boolean isInvader){
        super(id, x, y, z, isNest, isInvader);
        this.isNest=isNest;
        this.isInvader=isInvader;
        setViewDistance(300);

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

    /**
     * PRE: birds is not null, there are other initialized, valid birds
     * POST: The invader tries to group up with near flocks, thats why there is no seperation or alignment in it
     * @param birds
     */
    @Override
    public void flock(List<IBird> birds){
        view(birds);

        Vector cohesion = unite(birds);
        cohesion = cohesion.multiply(1.0f);
        addForce(cohesion);

        boolean movingOutOfWindow = movingOutOfWindow();
        if(movingOutOfWindow){
            float originalZ = getVelocity().getZ();
            setVelocity(getVelocity().invert().multiply(2));
            getVelocity().setZ(originalZ); // Don't alter height when moving out sideways
            addForce(getVelocity());
        }
        if(movingOutOfHeightBounds()){
            getVelocity().setZ(-getVelocity().getZ() * 2);
            addForce(getVelocity());
        }
    }

    @Override
    public Vector unite(List<IBird> birds){
        float maxDistance = 50;
        float multiplicator = 1;
        Vector goal = new Vector();
        int counter = 0;

        for(IBird bird : birds){
            if(!bird.isIncludedInSight()){
                continue;
            }
            float distance = this.distanceToBird(bird);

            if(distance > 0f && distance < maxDistance){
                goal = goal.add(bird.getPosition());
                counter++;
            }
        }
        if(counter > 0){
            goal = goal.divide(counter);
            return look(goal).multiply(multiplicator);
        }
        return goal.multiply(multiplicator);
    }


    @Override
    public void draw(Graphics2D g, AffineTransform at){
        float height = getPosition().getZ();
        int brightness = 0;
        if(height > 0.0f){
            brightness = (int) (height * 255 / 100);
        }
        g.setTransform(at);
        g.setColor(new Color(255, brightness, brightness));
        g.fillRect((int) getPosition().getX(), (int) getPosition().getY(), 15, 15);

    }

    public boolean isNest() {
        return isNest;
    }
    public boolean isInvader(){
        return isInvader;
    }


    @Override
    public int getDelay() {
        return 10000;
    }
}
