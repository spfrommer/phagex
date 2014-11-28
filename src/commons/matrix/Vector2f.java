package commons.matrix;

/**
 * An immutable 2d column Vector.
 */
public class Vector2f extends Matrix {
	public Vector2f(float x, float y) {
		super(2, 1, new float[] { x, y });
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