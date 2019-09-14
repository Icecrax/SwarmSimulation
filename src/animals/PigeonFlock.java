package animals;

import util.Util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Steliyan Syarov 11712196, David Scherer 11777743, Max Graf 01527246
 * @date 30.10.2018
 *
 * @brief: A class representing a flock of pigeons
 */

public class PigeonFlock implements IBirdFlock {

    private List<IBird> members;

    /**
     * POST: Creates a list of IBirds objects with arbitrary coordinates inside the world
     * @param num number of birds to spawn should be > 0
     * @param width VAR: world's width
     * @param height VAR: world's height
     */
    public PigeonFlock(int num, int width, int height) {
        members = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            this.addMember(
                    new Pigeon(i, Util.randomInRange(0, width), Util.randomInRange(0, height), Util.randomInRange(20, 60)));
        }
    }

    /**
     * PRE: g and at are not null
     * POST: All birds in the flock are updated and drawn to the screen
     * @param g
     * @param at
     */
    public void run(Graphics2D g, AffineTransform at) {
        for (IBird b : members) {
            b.run(g, members, at);
        }
    }

    boolean isOutOfWindow() {
        int c = 0;
        for (IBird member : members){
            if(member.isOutOfWindow()){
                c++;
            }
        }
        return c == members.size();
    }

    /**
     * PRE: members is not null
     * POST: A new bird is added to the list of birds
     * @param member
     */
    public void addMember(IBird member) {
        members.add(member);
    }
}
