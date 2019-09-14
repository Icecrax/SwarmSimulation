package util;

/**
 * @author Steliyan Syarov 11712196
 * @date 30.10.2018
 *
 * @brief The class is used for various vector calculations.
 */
public class Vector {
    private float x;
    private float y;
    private float z;

    /**
     * POST: private variables x, y, z are initialized
     * @param x 0 <= x && x <= WIDTH
     * @param y 0 <= y && y <= HEIGHT
     * @param z 0 <= z
     */
    public Vector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * POST: private variables x, y, z are initialized with 0.
     */
    public Vector(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * @return the vector normalized
     */
    public Vector normalize(){
        Vector normalized = new Vector(0, 0, 0);
        float betrag = getBetrag();
        if(betrag != 0){
            normalized.setX(x / betrag);
            normalized.setY(y / betrag);
            normalized.setZ(z / betrag);
        }
        return normalized;
    }

    /**
     * @return the magnitude of the vector
     */
    public float getBetrag(){
        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    /**
     * @return the inverted vector
     */
    public Vector invert(){
        return new Vector(-x,  -y, -z);
    }

    /**
     * @return this vector
     */
    public Vector getVector(){
        return this;
    }

    /**
     * @return the x coordinate of the vector
     */
    public float getX() {
        return x;
    }

    /**
     * POST: sets the x coordinate of the vector to the given
     * @param x 0 <= x && x <= WIDTH
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return the y coordinate of the vector
     */
    public float getY() {
        return y;
    }

    /**
     * POST: sets the y coordinate of the vector to the given
     * @param y 0 <= y && y <= HEIGHT
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return the z coordinate of the vector
     */
    public float getZ() {
        return z;
    }

    /**
     * POST: sets the z coordinate of the vector to the given
     * @param z 0 <= z
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     * @param vector the given vector is not NULL
     * @return Adds this vector and the given and returns the result as a new vector
     */
    public Vector add(Vector vector){
        return new Vector(this.x + vector.x, this.y + vector.y, this.z + vector.z);
    }

    /**
     * @param number the given skalar should be >= 0
     * @return Multiplies this vector with a given skalar and returns the result as a new vector
     */
    public Vector multiply(float number){
        return new Vector(x * number, y * number, z*number);
    }

    /**
     * @param vector the given vector is not NULL
     * @return Subtracts the given vector from this one and returns the result as a new vector
     */
    public Vector subtract(Vector vector){
        return new Vector(this.x - vector.x, this.y - vector.y, this.z - vector.z);
    }

    /**
     * @param other the given vector is not NULL
     * @return distance between the given vector and this one
     */
    public float distanceToVec(Vector other){
        return (float) Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2) + Math.pow(this.z - other.z, 2));
    }

    /**
     * @param v the given vector is not NULL
     * @return the dot product of this vector with the given one
     */
    private float dot(Vector v) {
        return x * v.x + y * v.y + x * v.z;
    }

    /**

     * @param divisor divisor is != 0
     * @return Divides this vector with a number and returns the result as a new vector
     */
    public Vector divide(float divisor){
        return new Vector(x / divisor, y / divisor, z / divisor);
    }

    /**
     * @param other the given vector is not NULL
     * @return the angle between this vector and the given in radians, where the result >= 0
     */
    public float angle(Vector other){
        return (float) Math.acos(this.dot(other) / (this.getBetrag() * other.getBetrag()));
    }
}
