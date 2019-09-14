package animals;

import util.Util;
import util.Vector;

import java.util.List;
/**
 * @author Steliyan Syarov 11712196
 * @date 30.10.2018
 *
 * @brief: A class representing a pigeon, a type of a bird
 */

public class Pigeon extends Bird {
    private final float formationDist, formationAngle;
    private float fullEnergy, currentEnergy;
    private final int sizeOfBird;

    /**
     * Initializes a pigeon object
     * @param id unique id number
     * @param x 0 <= x && x <= WIDTH
     * @param y 0 <= y && y <= HEIGHT
     * @param z 0 <= z
     */
    Pigeon(int id, float x, float y, float z) {
        super(id, x, y, z, false, false);

        setMaxVelocity(1.2f);

        setViewDistance(70f);
        setViewAngle((float) (Math.PI*0.75));

        formationDist = 50f;
        formationAngle = (float)(Math.PI/6);

        setWeightSeparation(1.5f);
        setWeightAlignment(1f);
        setWeightCohesion(0.2f);

        fullEnergy = 180;
        currentEnergy = fullEnergy;
        sizeOfBird = 3;
    }

    /**
     * POST: animals.Bird's position is updated according to its velocity, which in turn is affected by the bird's acceleration and the bird's energy reserve
     * INV: animals.Bird's position must not be outside of window bounds
     */
    public void update(){
        setVelocity(getVelocity().add(getAcceleration()));

        float usedEnergy = calculateKineticEnergy(getVelocity());
        if(usedEnergy > 45){
            fullEnergy -= usedEnergy;
            if(currentEnergy < 0) currentEnergy = 0;
        }
        else {
            currentEnergy += 5;
        }
        float energyScalar = Util.transform(currentEnergy, 0,fullEnergy,0, getMaxVelocity());
        setVelocity(getVelocity().multiply(energyScalar));

        if(getVelocity().getBetrag() > getMaxVelocity()){
            setVelocity(getVelocity().multiply(getMaxVelocity() / getVelocity().getBetrag()));
        }
        setPosition(getPosition().add(getVelocity()));
        setAcceleration(getAcceleration().multiply(0));
    }

    /**
     * @param velocity given vector is not NULL
     * @return energy measurement, depending on the passed speed and the size/mass of the object
     */
    private float calculateKineticEnergy(Vector velocity){
        return (float)(Math.pow(velocity.getBetrag(),2)*sizeOfBird/2);
    }

    /**
     * POST: The bird orients itself by the visible from it birds. An additional force influence it's behavior
     * @param birds birds is not NULL
     */
    @Override
    public void flock(List<IBird> birds){
        super.flock(birds);
        Vector formation = format(birds);
        formation = formation.multiply(0.2f);
        addForce(formation);
    }

    /**
     * @param birds birds is not NULL
     * @return An additional Vector, that influences the behavior of the bird and globally the structure of the flock
     */
    private Vector format(List<IBird> birds) {
        Pigeon tempBird = new Pigeon(-1,getPosition().getX(),getPosition().getY(), getPosition().getZ());
        for (IBird pigeon : birds) {

            if (!pigeon.isIncludedInSight()) continue;

            Vector toOtherPigeon = pigeon.getPosition().subtract(tempBird.getPosition());
            float angle = toOtherPigeon.angle(getVelocity());
            if (angle > formationAngle) continue;

            float distanceToPigeon = tempBird.distanceToBird(pigeon);
            if (distanceToPigeon > formationDist || distanceToPigeon <= 0) continue;

            Vector difference = tempBird.getPosition().subtract(pigeon.getPosition());
            difference = difference.normalize();
            difference = difference.multiply(formationDist - distanceToPigeon);
            tempBird.getPosition().add(difference);
            tempBird.view(birds);
        }

        return tempBird.getPosition().subtract(getPosition());
    }

}
