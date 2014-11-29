package commons;

import commons.matrix.DimensionException;
import commons.matrix.Matrix;
import commons.matrix.MatrixUtils;
import commons.matrix.Vector2f;

/**
 * A translate, rotation, and scale in a 2 dimensional space.
 * 
 * Order applied: translate, rotation, scale.
 */
public class Transform2f {
	private Vector2f m_translate;
	private float m_rotate;
	private Vector2f m_scale;

	/**
	 * An empty constructor which will zero out translation and rotation and initialize a scale of (1, 1).
	 */
	public Transform2f() {
		m_translate = new Vector2f(0, 0);
		m_rotate = 0;
		m_scale = new Vector2f(1, 1);
	}

	/**
	 * Creates a new Transform with the desired parameters.
	 * 
	 * @param translate
	 * @param rotate
	 * @param scale
	 */
	public Transform2f(Vector2f translate, float rotate, Vector2f scale) {
		m_translate = translate;
		m_rotate = rotate;
		m_scale = scale;
	}

	/**
	 * Creates a clone of another Transform2f.
	 * 
	 * @param transform
	 */
	public Transform2f(Transform2f transform) {
		m_translate = transform.getTranslation();
		m_rotate = transform.getRotation();
		m_scale = transform.getScale();
	}

	/**
	 * Creates a new Transform from an affine transform Matrix.
	 * 
	 * @param matrix
	 */
	public Transform2f(Matrix matrix) {
		if (matrix.getRows() != 3 || matrix.getColumns() != 3)
			throw new DimensionException();

		m_translate = MatrixUtils.getTranslate(matrix);
		m_rotate = MatrixUtils.getRotation(matrix);
		m_scale = MatrixUtils.getScale(matrix);
	}

	/**
	 * @return the translatation
	 */
	public Vector2f getTranslation() {
		return m_translate;
	}

	/**
	 * @param translate
	 */
	public void setTranslation(Vector2f translate) {
		m_translate = translate;
	}

	/**
	 * @return the rotatation
	 */
	public float getRotation() {
		return m_rotate;
	}

	/**
	 * @param rotate
	 */
	public void setRotation(float rotate) {
		m_rotate = rotate;
	}

	/**
	 * @return the scale
	 */
	public Vector2f getScale() {
		return m_scale;
	}

	/**
	 * @param scale
	 */
	public void setScale(Vector2f scale) {
		m_scale = scale;
	}

	@Override
	public String toString() {
		return getTranslation() + "\n" + getRotation() + "\n" + getScale();
	}
}
