package animals;

import util.Vector;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * @author Steliyan Syarov 11712196, David Scherer 11777743, Max Graf 01527246
 * Interface for all movable simulation elements
 */
public interface IBird {

    /**
     * Update bird and draw it to the screen
     * HISTORY CONSTRAINT: run has to call at first flock, then update, then draw
     * @param g
     * @param birds
     * @param at
     */
    void run(Graphics2D g, List<IBird> birds, AffineTransform at);

    /**
     * Draw bird to the screen
     * @param g
     * @param at
     */
    void draw(Graphics2D g, AffineTransform at);

    /**
     * orient bird according to other birds
     * @param birds
     */
    void flock(List<IBird> birds);

    /**
     * Return a direction the current bird can use to orient by other birds
     * @param goal
     * @return
     */
    Vector look(Vector goal);

    /**
     * Increase the acceleration by the specified force vector
     * @param force
     */
    void addForce(Vector force);

    /**
     * Perform the bird's position update using the velocity vector
     * HISTORY CONSTRAINT: must be called after {@link #flock(List)} and before {@link #draw(Graphics2D, AffineTransform)}
     */
    void update();

    /**
     * Return a vector to adjust the current bird's position in order to keep its minimum distance
     * from other birds
     * @param birds
     * @return
     */
    Vector separate(List<IBird> birds);

    /**
     * Returned a vector that specifies how the current bird should move in order to align with the birds around it
     * @param birds
     * @return
     */
    Vector align(List<IBird> birds);

    /**
     * Return a vector that specifies how the current bird should move in order to keep flock together
     * @param birds
     * @return
     */
    Vector unite(List<IBird> birds);

    /**
     * Mark all of the other birds as those that are included the current bird's sight and those
     * that are not
     * @param birds
     */
    void view(List<IBird> birds);

    /**
     * return the distance in space to another bird
     * @param bird
     * @return
     */
    float distanceToBird(IBird bird);

    Vector getPosition();

    void setPosition(Vector position);

    boolean isIncludedInSight();

    void setIncludedInSight(boolean includedInSight);

    Vector getVelocity();

    void setVelocity(Vector velocity);

    Vector getAcceleration();

    void setAcceleration(Vector acceleration);

    int getId();

    /**
     * Return a boolean specifying if the bird's position is out of the window bounds
     */
    boolean isOutOfWindow();

    /**
     * Return a boolean specifying if the bird would be moving out of the window bounds
     * if the calculated velocity is added to its current position
     */
    boolean movingOutOfWindow();

    /**
     * Return a boolean specifying if the bird would be moving out of the height bounds
     * if the calculated velocity is added to its current position
     */
    boolean movingOutOfHeightBounds();

    /**
     * Return if the current bird is of type {@link Nest}
     */
    boolean isNest();

    /**
     * Return if the current bird is of type {@link Invader}
     */
    boolean isInvader();

    float getMaxForce();

    float getMaxVelocity();

    void setMaxVelocity(float maxVelocity);

    void setViewDistance(float viewDistance);

    void setViewAngle(float viewAngle);

    void setWeightSeparation(float weightSeparation);

    void setWeightAlignment(float weightAlignment);

    void setWeightCohesion(float weightCohesion);
}
