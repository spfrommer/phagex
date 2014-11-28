package commons.matrix;

public class MatrixFactory {
	private MatrixFactory() {

	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @return the affine translation Matrix
	 */
	public static Matrix affineTranslate(float x, float y) {
		return new Matrix(3, 3, new float[] { 1, 0, x, 0, 1, y, 0, 0, 1 });
	}

	/**
	 * @param translation
	 * @return the affine translation Matrix
	 */
	public static Matrix affineTranslate(Vector2f translation) {
		return affineTranslate(translation.getX(), translation.getY());
	}

	/**
	 * @param rot
	 * @param axis
	 * @return the counter-clockwise affine rotation Matrix
	 */
	public static Matrix affineRotation(float rot) {
		float sin = (float) Math.sin(rot);
		float cos = (float) Math.cos(rot);

		return new Matrix(3, 3, new float[] { cos, -sin, 0, sin, cos, 0, 0, 0, 1 });
	}

	/**
	 * @param scaleX
	 * @param scaleY
	 * @return a scaling Matrix
	 */
	public static Matrix affineScale(float scaleX, float scaleY) {
		return new Matrix(3, 3, new float[] { scaleX, 0, 0, 0, scaleY, 0, 0, 0, 1 });
	}
}
