package commons.matrix;

/**
 * An immutable 3d column Vector.
 */
public class Vector3f extends Matrix {
	public Vector3f(float x, float y, float z) {
		super(3, 1, new float[] { x, y, z });
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
	 * @return the z value of the Vector
	 */
	public float getZ() {
		return this.getVal(2, 0);
	}

	/**
	 * Returns a Vector2f from the x and y components of this Vector.
	 * 
	 * @return
	 */
	public Vector2f unaugment() {
		return new Vector2f(getX(), getY());
	}

	/**
	 * @param vector
	 * @return the cross product of the two vectors
	 */
	public Vector3f crossProduct(Vector3f vector) {
		float x = getY() * vector.getZ() - getZ() * vector.getY();
		float y = getZ() * vector.getX() - getX() * vector.getZ();
		float z = getX() * vector.getY() - getY() * vector.getX();
		return new Vector3f(x, y, z);
	}

	@Override
	public String toString() {
		return "[" + getX() + ", " + getY() + ", " + getZ() + "]";
	}
}