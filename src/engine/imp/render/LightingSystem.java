package engine.imp.render;

import commons.Transform2f;

import engine.core.Entity;
import engine.core.EntityFilter;
import engine.core.EntitySystem;
import glextra.renderer.Renderer2D;

/**
 * Draws Lights to a RenderSystem.
 */
public class LightingSystem implements EntitySystem {
	private static final EntityFilter s_filter = new EntityFilter(new String[] { CLight.NAME }, new String[0],
			new String[0]);

	private RenderingSystem m_render;

	/**
	 * Makes a LightingSystem which draws Lights on a RenderSystem.
	 * 
	 * @param render
	 */
	public LightingSystem(RenderingSystem render) {
		m_render = render;
	}

	@Override
	public void update(float time) {

	}

	@Override
	public void updateEntity(Entity entity) {
		Renderer2D renderer = m_render.getRenderer();

		Transform2f transform = entity.getCTransform().getTransform();
		CLight light = (CLight) entity.components().get(CLight.NAME);

		renderer.pushModel();
		renderer.translate(transform.getTranslation().getX(), transform.getTranslation().getY());
		renderer.rotate(transform.getRotation());
		renderer.scale(transform.getScale().getX(), transform.getScale().getY());

		renderer.renderLight(light.getLight());
		// renderer.renderLight(new PointLight(new Vector3f(0f, 0f, 1f), new Vector3f(0.1f, 0.05f, 0.0001f), new
		// Color(1f,
		// 0f, 0f), new Color(0f, 0f, 1f)));

		renderer.popModel();
	}

	@Override
	public void postUpdate() {

	}

	@Override
	public EntityFilter getFilter() {
		return s_filter;
	}
}