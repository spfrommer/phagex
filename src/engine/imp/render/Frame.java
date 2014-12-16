package engine.imp.render;

/**
 * A frame in an animation.
 */
public class Frame {
	private Material2D m_material;
	private float m_time;

	public Frame(Material2D material, float time) {
		if (material == null)
			throw new AnimationException("Cannot create frame with null material!");
		if (time < 0)
			throw new AnimationException("Time per frame cannot be less than zero!");
		m_material = material;
		m_time = time;
	}

	/**
	 * Copies a Frame.
	 * 
	 * @param frame
	 */
	public Frame(Frame frame) {
		m_material = frame.getMaterial();
		m_time = frame.getTime();
	}

	/**
	 * @return the Material
	 */
	public Material2D getMaterial() {
		return m_material;
	}

	/**
	 * Sets the Material.
	 * 
	 * @param material
	 */
	public void setMaterial(Material2D material) {
		if (material == null)
			throw new AnimationException("Cannot set null material on Frame!");
		m_material = material;
	}

	/**
	 * @return the time the frame should be shown in milliseconds
	 */
	public float getTime() {
		return m_time;
	}

	/**
	 * Sets the time the frame should be shown in milliseconds.
	 * 
	 * @param time
	 */
	public void setTime(float time) {
		if (time < 0)
			throw new AnimationException("Time per frame cannot be less than zero!");
		m_time = time;
	}
}
