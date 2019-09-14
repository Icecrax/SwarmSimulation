package animals;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * @author Steliyan Syarov 11712196
 */
public interface IBirdFlock {

    /**
     * Updates all birds in the flock and draws them to the screen
     * @param g
     * @param at
     */
    void run(Graphics2D g, AffineTransform at);

    /**
     * Adds a member from type animals.IBird to a list of IBirds
     * @param bird
     */
    void addMember(IBird bird);

}
