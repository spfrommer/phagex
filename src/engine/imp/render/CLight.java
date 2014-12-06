package engine.imp.render;

import engine.core.Component;
import engine.core.ComponentBuilder;
import engine.core.exceptions.ComponentException;
import glextra.renderer.Light;

/**
 * Contains a Light in the Scene.
 */
public class CLight implements Component {
	public static final String NAME = "light";
	public static final String LIGHT = "render_light";
	private static final String[] IDENTIFIERS = new String[] { LIGHT };

	private Light m_light;

	/**
	 * Initializes the light Component with a Light.
	 * 
	 * @param light
	 */
	public CLight(Light light) {
		m_light = light;
	}

	/**
	 * @return the Light
	 */
	public Light getLight() {
		return m_light;
	}

	/**
	 * @param light
	 *            the Light to set
	 */
	public void setLight(Light light) {
		m_light = light;
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

		if (identifier.equals(LIGHT))
			return m_light;

		throw new ComponentException("No such identifier!");
	}

	@Override
	public void setData(String identifier, Object data) {
		if (identifier == null)
			throw new ComponentException("Cannot set data for null identifier!");
		if (data == null)
			throw new ComponentException("Cannot set null data for identifier: " + identifier);

		if (identifier.equals(LIGHT)) {
			m_light = (Light) data;
		} else {
			throw new ComponentException("No data for identifier: " + identifier);
		}
	}

	@Override
	public ComponentBuilder<CLight> getBuilder() {
		ComponentBuilder<CLight> builder = new ComponentBuilder<CLight>() {
			@Override
			public CLight build() {
				return new CLight(LightFactory.clone(m_light));
			}

			@Override
			public String getName() {
				return NAME;
			}
		};
		return builder;
	}
}
