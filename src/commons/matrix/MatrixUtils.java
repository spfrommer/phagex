package commons.matrix;

/**
 * Utility functions for Matrices.
 */
public class MatrixUtils {
	private MatrixUtils() {

	}

	/**
	 * Gets the translation portion of the affine transform matrix.
	 * 
	 * @param affineTransform
	 * @return the translation portion of the Matrix
	 */
	public static Vector2f getTranslate(Matrix affineTransform) {
		if (affineTransform.getRows() != 3 || affineTransform.getColumns() != 3)
			throw new DimensionException();
		return new Vector2f(affineTransform.getVal(0, 2), affineTransform.getVal(1, 2));
	}

	/**
	 * Gets the rotation part of the affine transform matrix. Adapted from gltools.
	 * 
	 * @param affineTransform
	 * @return the rotation portion of the Matrix
	 */
	public static float getRotation(Matrix affineTransform) {
		if (affineTransform.getRows() != 3 || affineTransform.getColumns() != 3)
			throw new DimensionException();
		float a = affineTransform.getVal(0, 0);
		float b = affineTransform.getVal(0, 1);
		return (float) Math.atan2(-b, a);
	}

	/**
	 * Gets the scale portion of the affine transform matrix. Adapted from gltools.
	 * 
	 * Perpendicular rotations cause a bug where the diagonals have rounding errors and throw off the scale calculation.
	 * 
	 * @param affineTransform
	 * @return the scale portion of the Matrix
	 */
	public static Vector2f getScale(Matrix affineTransform) {
		if (affineTransform.getRows() != 3 || affineTransform.getColumns() != 3)
			throw new DimensionException();
		float a = affineTransform.getVal(0, 0);
		float b = affineTransform.getVal(0, 1);
		float c = affineTransform.getVal(1, 0);
		float d = affineTransform.getVal(1, 1);

		float sX = (float) (MathUtils.sign(a) * Math.sqrt(a * a + b * b));
		float sY = (float) (MathUtils.sign(d) * Math.sqrt(c * c + d * d));
		return new Vector2f(sX, sY);
	}
}
