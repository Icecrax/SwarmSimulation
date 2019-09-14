package util;

import animals.IBird;

import java.awt.*;
import java.util.Random;

/**
 * @author Max Graf 01527246
 * @date 30.10.2018
 */
public class Util {

    private static Dimension dimension;

    /**
     * POST: A number between min and max which is not zero is returned
     * @param min
     * @param max
     * @return
     */
    public static int randomInRange(int min, int max) {
        Random r = new Random();
        int rand = r.nextInt((max - min) + 1) + min;
        return rand == 0 ? randomInRange(min, max) : rand;
    }

    /**
     * PRE: dim is not null
     * POST: window dimensions are set to the specified width and height
     * @param dim
     */
    public static void setDimension(Dimension dim){
        dimension = new Dimension(dim.width, dim.height);
    }

    public static Dimension getDimension(){
        return dimension;
    }


    /**
     * PRE: bird is not null
     * @param bird
     * @return
     */
    public static boolean isOutOfWindow(IBird bird){
        Vector position = bird.getPosition();
        return position.getX() > (float) getDimension().getWidth()
            || position.getY() > (float) getDimension().getHeight()
            || position.getX() < 0f
            || position.getY() < 0f;
    }

    /**
     * PRE: bird is not null
     * @param bird
     * @return
     */
    public static boolean isMovingOutOfWindow(IBird bird){
        Vector position = bird.getPosition();
        Vector velocity = bird.getVelocity();
        return position.getX() + velocity.getX() > (float) getDimension().getWidth()
            || position.getY() + velocity.getY() > (float) getDimension().getHeight()
            || position.getX() + velocity.getX() < 0f
            || position.getY() + velocity.getY() < 0f;
    }

    /**
     * @author Steliyan Syarov
     * @param x any number
     * @return The function produces for a given number a value between 0 and 1
     */
    public static float noise(float x){
        return (float)Math.sin((Math.pow(Math.E,x/(Math.pow(Math.E,x)+1) + Math.sin(x)) + Math.sin(x/2))/2);
    }

    /**
     * @author Steliyan Syarov
     * @param number number from the interval [_lwBound, _upBound]
     * @param _lwBound _lwBound should be less than _upBound
     * @param _upBound _upBound should be bigger than _lwBound
     * @param lwBound lwBound should be less than upBound
     * @param upBound upBound should be bigger than the lwBound
     * @return the transformed number from the first interval into the second one
     */
    public static float transform(float number,float _lwBound, float _upBound, float lwBound, float upBound){
        float _diff = _upBound - _lwBound;
        float diff = upBound - lwBound;
        float step = (diff/_diff);
        return number * step;
    }
}
