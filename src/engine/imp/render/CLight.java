package engine.imp.render;

import engine.core.Component;
import engine.core.ComponentBuilder;
import glextra.renderer.Light;

/**
 * Contains a Light in the Scene.
 */
public class CLight implements Component {
	public static final String NAME = "light";

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
