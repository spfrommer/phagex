package engine.imp.render;

import engine.core.Component;
import engine.core.ComponentBuilder;

/**
 * A camera component - contains a field of view.
 */
public class CCamera implements Component {
	public static final String NAME = "camera";

	private float m_scale;
	private boolean m_lookThrough = false;

	private RenderingSystem m_render;

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
	 * @param scale
	 *            the scale - 1f is default
	 * @param lookThrough
	 *            whether the camera should be used to render the Scene
	 */
	public CCamera(float scale, boolean lookThrough) {
		if (scale <= 0)
			throw new RenderingException("Cannot have a scale of less than or equal to zero!");

		m_scale = scale;
		m_lookThrough = lookThrough;
	}

	/**
	 * @param system
	 *            the RenderingSystem to forward calls to
	 */
	protected void setRenderingSystem(RenderingSystem system) {
		m_render = system;
	}

	/**
	 * @param lookThrough
	 *            whether the Camera is being used to render the Scene.
	 */
	protected void setLookThrough(boolean lookThrough) {
		m_lookThrough = lookThrough;
	}

	/**
	 * Sets the camera to be looked through. This is undone if lookThrough is called on another CCamera. Only call this
	 * after the Game has been started.
	 */
	public void lookThrough() {
		m_lookThrough = true;
		if (m_render != null)
			m_render.lookThroughCalled(this);
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
	public ComponentBuilder<CCamera> getBuilder() {
		ComponentBuilder<CCamera> builder = new ComponentBuilder<CCamera>() {
			@Override
			public CCamera build() {
				return new CCamera(m_scale, m_lookThrough);
			}

			@Override
			public String getName() {
				return NAME;
			}
		};
		return builder;
	}
}
