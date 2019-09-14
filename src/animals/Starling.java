package animals;

import util.AirCurrent;
import util.Vector;

import java.util.List;
/**
 * @author Steliyan Syarov 11712196, David Scherer 11777743, Max Graf 01527246
 * @date 30.10.2018
 *
 * @brief: A class representing a starling, a type of a bird
 */

public class Starling extends Bird {

    private AirCurrent current;

    /**
     * @brief Initialize a starling object
     * @param id unique id number
     * @param x 0 <= x && x <= WIDTH
     * @param y 0 <= y && y <= HEIGHT
     * @param z 0 <= z
     * @param current initialized AirCurrent object, which will affect the bird's flight
     */
    Starling(int id, float x, float y, float z, AirCurrent current) {
        super(id, x, y, z, false, false);

        setMaxVelocity(1.4f);
        setViewDistance(50f);
        setViewAngle((float) (Math.PI * 0.7));

        setWeightSeparation(2.5f);
        setWeightAlignment(1.5f);
        setWeightCohesion(0.7f);

        this.current = current;
    }

    /**
     * POST: There is the possibility of wind affecting the flight of the birds
     * @param birds initialized list of IBirds
     */
    @Override
    public void flock(List<IBird> birds){
        super.flock(birds);

        if(current.isWind()){
            addForce(wind(current));
        }
    }

    /**
     * @param flow object from class AirCurrent, holding a matrix filled with direction vectors
     * @return Finds the corresponding to the position of the bird vector from the AirCurrent matrix and calculates a vector,
     * which will orient the bird in the direction the wind is blowing
     */
    private Vector wind(AirCurrent flow) {
        Vector direction = flow.lookup(getPosition());
        direction.multiply(getMaxVelocity());
        direction = direction.subtract(getVelocity());
        if(direction.getBetrag() > getMaxForce()){
            direction = direction.multiply(getMaxForce() / direction.getBetrag());
        }
        return direction;
    }
}
