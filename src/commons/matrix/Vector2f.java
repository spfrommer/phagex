package commons.matrix;

/**
 * An immutable 2d column Vector.
 */
public class Vector2f extends Matrix {
	/**
	 * Creates a Vector2f.
	 * 
	 * @param x
	 * @param y
	 */
	public Vector2f(float x, float y) {
		super(2, 1, new float[] { x, y });
	}

	/**
	 * Creates a unit vector from the angle.
	 * 
	 * @param angle
	 */
	public Vector2f(float angle) {
		super(2, 1, new float[] { (float) -Math.sin(angle), (float) Math.cos(angle) });
	}

	/**
	 * @return the x value of the Vector
	 */
	public float getX() {
		return this.getVal(0, 0);
	}

	/**
	 * @return the y value of the Vector
	 */
	public float getY() {
		return this.getVal(1, 0);
	}

	/**
	 * @return the original vector + 1 for the Z component of the 3f matrix
	 */
	@Override
	public Vector3f augment() {
		return new Vector3f(getX(), getY(), 1);
	}

	/**
	 * @return the length of the Vector2f
	 */
	public float length() {
		return (float) Math.sqrt(getX() * getX() + getY() * getY());
	}

	/**
	 * Scales the Vector2f to the appropriate length and returns the scaled Vector2f.
	 * 
	 * @param length
	 * @return the scaled Vector2f
	 */
	public Vector2f setLength(float length) {
		return this.scalarMultiply(length / this.length()).toVector2f();
	}

	/**
	 * @return the angle in radians, with zero being north and positive going counter clockwise
	 */
	public float angle() {
		return (float) Math.atan2(-getX(), getY());
	}

	/**
	 * Returns the z dimension of the cross product of 2 vectors in the 2d plane (the x and y dimensions are obviously
	 * zero).
	 * 
	 * @param vector
	 * @return
	 */
	public float crossProduct(Vector2f vector) {
		return (getX() * vector.getY() - getY() * vector.getX());
	}

	@Override
	public String toString() {
		return "[" + getX() + ", " + getY() + "]";
	}
}