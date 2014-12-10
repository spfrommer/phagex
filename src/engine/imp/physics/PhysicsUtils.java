package engine.imp.physics;

import org.dyn4j.geometry.Vector2;

import commons.matrix.Vector2f;

/**
 * Utility methods for physics.
 */
public class PhysicsUtils {
	private PhysicsUtils() {

	}

	/**
	 * @param vector
	 * @return the converted vector
	 */
	public static Vector2 toDyn4j(Vector2f vector) {
		if (vector == null)
			throw new PhysicsException("Vector2f cannot be null!");
		return new Vector2(vector.getX(), vector.getY());
	}

	/**
	 * @param vector
	 * @return the converted vector
	 */
	public static Vector2f fromDyn4j(Vector2 vector) {
		if (vector == null)
			throw new PhysicsException("Dyn4j vector cannot be null!");
		return new Vector2f((float) vector.x, (float) vector.y);
	}
}
