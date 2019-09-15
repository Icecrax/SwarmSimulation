package animals;

import util.Util;
import util.Vector;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * @author Steliyan Syarov 11712196, David Scherer 11777743, Max Graf 01527246
 */
public class Bird implements IBird {

    private int id;
    private Vector position, velocity, acceleration;
    private float maxForce, maxVelocity, viewDistance, viewAngle, weightSeparation, weightAlignment, weightCohesion;
    private boolean includedInSight, isNest, isInvader;

    /**
     * PRE: id is unique, x, y and z are non-negative and not bigger than the world bounds
     * POST: A new bird is successfully created
     * @param id
     * @param x
     * @param y
     * @param z
     * @param isNest
     * @param isInvader
     */
    Bird(int id, float x, float y, float z, boolean isNest, boolean isInvader) {
        this.id = id;
        this.position = new Vector(x, y, z);
        this.velocity = new Vector(Util.randomInRange(0, 3), Util.randomInRange(0, 3), Util.randomInRange(0, 5));
        this.acceleration = new Vector();

        this.maxVelocity = 2.0f;
        this.maxForce = 0.05f;

        viewDistance = 200;
        viewAngle = (float) (Math.PI * 0.85);

        weightSeparation = 1.5f;
        weightAlignment = 1.5f;
        weightCohesion = 0.6f;

        this.isNest = isNest;
        this.isInvader = isInvader;
    }

    /**
     * PRE: animals.Bird is included in the list of all birds
     * POST: animals.Bird's position is updated according to its velocity, which in turn is affected by the bird's acceleration
     * INV: animals.Bird's position must not be outside of window bounds
     */
    public void update(){
        velocity = velocity.add(acceleration);
        if(velocity.getBetrag() > maxVelocity){
            velocity = velocity.multiply(maxVelocity / velocity.getBetrag());
        }
        if(velocity.getX() == 0){
            velocity.setX(Util.randomInRange(0, 3));
        }
        if(velocity.getY() == 0){
            velocity.setY(Util.randomInRange(0, 3));
        }
        position = position.add(velocity);
        acceleration = acceleration.multiply(0);
    }

    /**
     * PRE: Force is not null
     * POST: The force is added to the acceleration
     * @param force a vector to be added
     */
    public void addForce(Vector force){
        acceleration = acceleration.add(force);
    }

    /**
     * PRE: goal is not null
     * POST: Returns a direction the bird uses to find its way towards the goal
     * @param goal a vector at which to steer
     * @return
     */
    public Vector look(Vector goal){
        Vector direction = goal.subtract(position);
        direction = direction.normalize();
        direction = direction.multiply(maxVelocity);
        direction = direction.subtract(velocity);
        // limit
        if(direction.getBetrag() > maxForce){
            direction = direction.multiply(maxForce / direction.getBetrag());
        }

        return direction;
    }

    /**
     * PRE: birds is not null, there are other initialized, valid birds
     * POST: The bird oriented itself by other birds and is not out of the world bounds
     * @param birds a list of not NULL bird objects
     */
    public void flock(List<IBird> birds){
        view(birds);

        Vector separation = separate(birds);
        Vector alignment = align(birds);
        Vector cohesion = unite(birds);

        separation = separation.multiply(weightSeparation);
        alignment = alignment.multiply(weightAlignment);
        cohesion = cohesion.multiply(weightCohesion);

        addForce(separation);
        addForce(alignment);
        addForce(cohesion);

        boolean movingOutOfWindow = movingOutOfWindow();
        if(movingOutOfWindow){
            float originalZ = velocity.getZ();
            velocity = velocity.invert().multiply(2);
            velocity.setZ(originalZ);
            addForce(velocity);
        }
        if(movingOutOfHeightBounds()){
            velocity.setZ(-velocity.getZ() * 2);
            addForce(velocity);
        }
    }

    /**
     * PRE: birds is not null
     * POST: Mark all birds that are included in the current bird's sight
     * @param birds a list of not NULL bird objects
     */
    public void view(List<IBird> birds){

        int counter = 0; //for the capacity of the brain...a bird can only monitor 20 other
        for (IBird bird : birds){
            bird.setIncludedInSight(false);

            if(this == bird){
                continue;
            }

            float distanceToBird = this.distanceToBird(bird);
            if(distanceToBird <= 0 || distanceToBird > viewDistance){
                continue;
            }

            Vector toOtherBird = bird.getPosition().subtract(position);
            float angle = toOtherBird.angle(velocity);
            if(angle < viewAngle && counter < 20){
                bird.setIncludedInSight(true);
                counter++;
            }
            if(counter >= 20){
                return;
            }
        }
    }

    /**
     * PRE: birds is not null
     * POST: A vector is returned that specifies how the current bird should move in order to avoid collision with other birds
     * @param birds a list of not NULL bird objects
     * @return a vector which keeps the bird separated from the rest
     */
    public Vector separate(List<IBird> birds){
        float separation = 30;
        float multiplicator = 1;

        Vector change = new Vector();
        int counter = 0;
        for (IBird bird : birds){
            if(!bird.isIncludedInSight()){
                continue;
            }
            if (bird.isInvader()){
                multiplicator = 3;
                separation = 100;
            }

            float dist = this.distanceToBird(bird);
            if(dist > 0f && dist < separation){
                Vector difference = position.subtract(bird.getPosition());
                difference = difference.normalize();
                difference = difference.divide(dist);
                change = change.add(difference);
                counter++;
            }
        }

        if(counter > 0){
            change = change.divide(counter);
        }

        if(change.getBetrag() > 0){
            change = change.normalize();
            change = change.multiply(maxVelocity);
            change = change.subtract(velocity);
            if(change.getBetrag() > maxForce){
                change = change.multiply(maxForce / change.getBetrag());
            }
        }
        change = change.multiply(multiplicator);
        return change;
    }

    /**
     * PRE: birds is not null
     * POST: A vector is returned that specifies how the current bird should move in order to align with the birds around it
     * @param birds a list of not NULL bird objects
     * @return a vector which keeps the bird aligned with the rest
     */
    public Vector align(List<IBird> birds){

        float maxDist = 50;
        float multiplicator = 1;

        Vector change = new Vector();
        int counter = 0;
        for (IBird bird : birds){
            if(!bird.isIncludedInSight()){
                continue;
            }
            float dist = this.distanceToBird(bird);


            if(dist > 0f && dist < maxDist){
                change = change.add(bird.getVelocity());
                counter++;
            }
        }

        if(counter > 0){
            change = change.divide(counter);
            change = change.normalize();
            change = change.multiply(maxVelocity);
            change = change.subtract(velocity);
            if(change.getBetrag() > maxForce){
                change = change.multiply(maxForce / change.getBetrag());
            }
        }
        return change.multiply(multiplicator);
    }

    /**
     * PRE: birds is not null
     * POST: A vector is returned that specifies how the current bird should move in order to keep flock together
     * @param birds a list of not NULL bird objects
     * @return a vector which keeps the bird close to the rest
     */
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

            if (bird.isNest() && distance > 0f && distance < maxDistance){
                this.maxForce = 10;
                goal = (goal.add(bird.getPosition().multiply(15f))).multiply(20);
                counter++;
                continue;
            } else {
                this.maxForce = 0.05f;
            }

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

    /**
     * PRE: g is not null, at is not null
     * POST: The representation of the current bird is drawn in 2D space
     * @param g a Graphics2D object
     * @param at an AffineTransform object
     */
    public void draw(Graphics2D g, AffineTransform at){
        int brightness = 0;
        float height = getPosition().getZ();
        if(height > 0.0f){
            brightness = (int) (height * 255 / 100);
        }

        g.setTransform(at);

        // TODO:
        //  ERROR: If height is out of the height bounds, brightness becomes assigned an invalid value
        //  (not between 0 and 255)
        //  Could have been avoided by either limiting the bird's allowed position of limiting the
        //  brightness value
        g.setColor(new Color(255, brightness, brightness));
        g.fillRect((int) position.getX(), (int) position.getY(), 5, 5);
    }

    /**
     * PRE: g, birds and at are not null
     * POST: The current bird is updated according to the game mechanics and subsequently
     * drawn to the screen
     * @param g
     * @param birds
     * @param at
     */
    public void run(Graphics2D g, List<IBird> birds, AffineTransform at){
        flock(birds);
        update();
        draw(g, at);
    }

    /**
     * PRE: bird is not null
     * POST: The distance between the two birds' positions is returned
     * @param bird a not null bird object
     * @return the distance to that bird from the current object
     */
    public float distanceToBird(IBird bird){
        if(this.id == bird.getId()){
            return 0f;
        }
        return position.distanceToVec(bird.getPosition());
    }

    /**
     * POST: The return value specifies if the bird is out of the window bounds
     * @return if the current bird is out of window bounds
     */
    public boolean isOutOfWindow(){
        return Util.isOutOfWindow(this);
    }

    /**
     * POST: The return value specifies if the bird is moving out of the window bounds
     * @return if the current bird is moving out of window bounds
     */
    public boolean movingOutOfWindow(){
        return Util.isMovingOutOfWindow(this);
    }

    /**
     * POST: The return value specifies if the bird is out of the height bounds
     * @return if the current bird is out of the height bounds
     */
    public boolean movingOutOfHeightBounds(){
        return position.getZ() + velocity.getZ() > 100
            || position.getZ() + velocity.getZ() < 0f;
    }

    public int getId() {
        return id;
    }

    public Vector getPosition(){
        return position;
    }

    public void setPosition(Vector position){
        this.position = position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity){
        this.velocity = velocity;
    }

    public Vector getAcceleration(){
        return this.acceleration;
    }

    public void setAcceleration(Vector acceleration){
        this.acceleration = acceleration;
    }

    public boolean isNest() {
        return isNest;
    }

    public boolean isInvader(){
        return isInvader;
    }

    public boolean isIncludedInSight() {
        return includedInSight;
    }

    public void setIncludedInSight(boolean includedInSight) {
        this.includedInSight = includedInSight;
    }

    public float getMaxForce() {
        return maxForce;
    }

    public float getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public void setViewDistance(float viewDistance) {
        this.viewDistance = viewDistance;
    }

    public void setViewAngle(float viewAngle) {
        this.viewAngle = viewAngle;
    }

    public void setWeightSeparation(float weightSeparation) {
        this.weightSeparation = weightSeparation;
    }

    public void setWeightAlignment(float weightAlignment) {
        this.weightAlignment = weightAlignment;
    }

    public void setWeightCohesion(float weightCohesion) {
        this.weightCohesion = weightCohesion;
    }
}
