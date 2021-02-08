package com.stuypulse.stuylib.math;

/**
 * A class that is used to store polar coordinates. It's heavily dependant on the vector and angle
 * class.
 *
 * @author Sam Belliveau (sam.belliveau@gmail.com)
 */
public class Polar2D {

    // Configuration for toString() function
    private static final int STRING_SIGFIGS = 5;

    // Internal Variables
    public final double magnitude;
    public final Angle angle;

    /**
     * Create a Polar2D with a magnitude and an angle
     *
     * @param mag magnitude
     * @param ang angle
     */
    public Polar2D(double mag, Angle ang) {
        // Make sure the magnitude is always positive
        if(mag >= 0) {
            magnitude = mag;
            angle = ang;
        } else {
            magnitude = -mag;
            angle = ang.negative();
        }
    }

    /**
     * Convert a Vector2D into Polar Coordinates
     *
     * @param vec vector
     */
    public Polar2D(Vector2D vec) {
        this(vec.magnitude(), vec.getAngle());
    }

    /**
     * Get the magnitude of the Polar2D class
     *
     * @return magnitude of the Polar2D class
     */
    public double magnitude() {
        return magnitude;
    }

    /**
     * Get the angle of the Polar2D class
     *
     * @return angle of the Polar2D class
     */
    public Angle getAngle() {
        return angle;
    }

    /**
     * Convert polar coordinates into a Vector2D
     *
     * @return Polar Coordinates as a Vector2D
     */
    public Vector2D getVector() {
        return angle.getVector().mul(magnitude);
    }

    /**
     * Multiply the polar coordinates by scalar
     *
     * @param m scalar to multiply by
     * @return result of multiplication
     */
    public Polar2D mul(double m) {
        return new Polar2D(magnitude * m, angle);
    }

    /**
     * Divide the polar coordinates by scalar
     *
     * @param d scalar to divide by
     * @return result of division
     */
    public Polar2D div(double d) {
        return new Polar2D(magnitude / d, angle);
    }

    /**
     * Rotate the polar coordinates by angle
     *
     * @param a angle to rotate by
     * @return result of rotation
     */
    public Polar2D rotate(Angle a) {
        return new Polar2D(magnitude, angle.add(a));
    }

    /**
     * Return a Polar2D object with a negative magnitude
     *
     * @return Polar2D object with a negative magnitude
     */
    public Polar2D negative() {
        return new Polar2D(-magnitude, angle);
    }

    /**
     * Normalize the polar coordinates so the magnitude is 1
     *
     * @return normalized polar coordinates
     */
    public Polar2D normalize() {
        return new Polar2D(1, angle);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("Polar2D(");
        out.append(SLMath.round(magnitude, STRING_SIGFIGS));
        out.append(", ");
        out.append(angle);
        out.append(")");
        return out.toString();
    }

}