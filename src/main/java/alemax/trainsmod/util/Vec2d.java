package alemax.trainsmod.util;

public class Vec2d {

    public double x;
    public double y;

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2d() {
        this(0,0);
    }

    public Vec2d(Vec2d vec) {
        this(vec.x, vec.y);
    }

    public void add(Vec2d vec) {
        this.x += vec.x;
        this.y += vec.y;
    }

    public void scale(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    /**
     * Computes the dot product of the this vector and vector v1.
     * @param v1 the other vector
     */
    public final double dot(Vec2d v1)
    {
        return (this.x*v1.x + this.y*v1.y);
    }

    /**
     * Returns the length of this vector.
     * @return the length of this vector
     */
    public final double length() {
        return (double) Math.sqrt(this.x*this.x + this.y*this.y);
    }

    /**
     *   Returns the angle in radians between this vector and the vector
     *   parameter; the return value is constrained to the range [0,PI].
     *   @param v1    the other vector
     *   @return   the angle in radians in the range [0,PI]
     */
    public final double angle(Vec2d v1) {
        double vDot = this.dot(v1) / ( this.length()*v1.length() );
        if( vDot < -1.0) vDot = -1.0;
        if( vDot >  1.0) vDot =  1.0;
        return((double) (Math.acos( vDot )));
    }

    /**
     * Sets the value of this vector to the normalization of vector v1.
     * @param v1 the un-normalized vector
     */
    public final void normalize(Vec2d v1)
    {
        double norm;

        norm = (double) (1.0/Math.sqrt(v1.x*v1.x + v1.y*v1.y));
        this.x = v1.x*norm;
        this.y = v1.y*norm;
    }

    /**
     * Normalizes this vector in place.
     */
    public final void normalize()
    {
        double norm;

        norm = (double)
                (1.0/Math.sqrt(this.x*this.x + this.y*this.y));
        this.x *= norm;
        this.y *= norm;
    }

}
