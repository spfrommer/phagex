package engine.imp.render;

import engine.core.Component;
import engine.core.ComponentBuilder;
import engine.core.exceptions.ComponentException;

/**
 * A camera component - contains a field of view.
 */
public class CCamera implements Component {
	public static final String NAME = "camera";
	public static final String SCALE = "scale";
	private static final String[] IDENTIFIERS = new String[] { SCALE };

	private float m_scale;
	private boolean m_lookThrough = false;

	/**
	 * Constructs a CCamera with a scale of 1f.
	 */
	public CCamera() {
		m_scale = 1f;
	}

	/**
	 * @param scale
	 *            the scale - 1f is default
	 */
	public CCamera(float scale) {
		if (scale <= 0)
			throw new RenderingException("Cannot have a scale of less than or equal to zero!");
		m_scale = scale;
	}

	/**
	 * @param lookThrough
	 *            whether the Camera is being used to render the Scene.
	 */
	protected void setLookThrough(boolean lookThrough) {
		m_lookThrough = lookThrough;
	}

	/**
	 * Sets the camera to be looked through. This is undone if lookThrough is called on another CCamera.
	 */
	public void lookThrough() {
		m_lookThrough = true;
	}

	/**
	 * @return whether the CCamera is currently the one beign used to render the Scene.
	 */
	public boolean isLookedThrough() {
		return m_lookThrough;
	}

	/**
	 * @return the scale - 1f is default
	 */
	public float getScale() {
		return m_scale;
	}

	/**
	 * Sets the scale - 1f is default.
	 * 
	 * @param scale
	 */
	public void setScale(float scale) {
		if (scale <= 0)
			throw new RenderingException("Cannot have a FOV less than or equal to zero!");
		m_scale = scale;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String[] getIdentifiers() {
		return IDENTIFIERS;
	}

	@Override
	public Object getData(String identifier) {
		if (identifier == null)
			throw new ComponentException("Cannot get data for null identifier!");

		if (identifier.equals(SCALE))
			return m_scale;

		throw new ComponentException("No such identifier!");
	}

	@Override
	public void setData(String identifier, Object data) {
		if (identifier == null)
			throw new ComponentException("Cannot set data for null identifier!");
		if (data == null)
			throw new ComponentException("Cannot set null data for identifier: " + identifier);

		if (identifier.equals(SCALE)) {
			m_scale = (Float) data;
		} else {
			throw new ComponentException("No data for identifier: " + identifier);
		}
	}

	@Override
	public ComponentBuilder<CCamera> getBuilder() {
		ComponentBuilder<CCamera> builder = new ComponentBuilder<CCamera>() {
			@Override
			public CCamera build() {
				return new CCamera(m_scale);
			}

			@Override
			public String getName() {
				return NAME;
			}
		};
		return builder;
	}
}
