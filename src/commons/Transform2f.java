package commons;

import commons.matrix.Vector2f;

/**
 * A rotate, translate, and scale in a 2 dimensional space.
 * 
 * Order applied: rotation, translation, scale.
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
	 * @return the translatation
	 */
	public Vector2f getTranslatation() {
		return m_translate;
	}

	/**
	 * @param translate
	 */
	public void setTranslatation(Vector2f translate) {
		m_translate = translate;
	}

	/**
	 * @return the rotatation
	 */
	public float getRotatation() {
		return m_rotate;
	}

	/**
	 * @param rotate
	 */
	public void setRotatation(float rotate) {
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
}
