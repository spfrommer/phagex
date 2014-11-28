package commons.matrix;

public class MatrixFactory {
	private MatrixFactory() {

	}

	/**
	 * @param width
	 * @return an identity matrix
	 */
	public static Matrix identity(int width) {
		float[] newArray = new float[width * width];
		for (int r = 0; r < width; r++) {
			for (int w = 0; w < width; w++) {
				int arrayLoc = r * width + w;
				if (r == w) {
					newArray[arrayLoc] = 1;
				} else {
					newArray[arrayLoc] = 0;
				}
			}
		}
		return new Matrix(width, width, newArray);
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

	/**
	 * @param scale
	 * @return a scaling Matrix
	 */
	public static Matrix affineScale(Vector2f scale) {
		return affineScale(scale.getX(), scale.getY());
	}
}
