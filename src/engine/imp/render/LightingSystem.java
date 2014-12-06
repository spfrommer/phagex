package engine.imp.render;

import commons.Transform2f;

import engine.core.Entity;
import engine.core.SimpleEntityFilter;
import engine.core.EntitySystem;
import engine.core.Scene;
import engine.core.TreeNode;
import glextra.renderer.Renderer2D;

/**
 * Draws Lights to a RenderSystem.
 */
public class LightingSystem implements EntitySystem {
	private static final SimpleEntityFilter s_filter = new SimpleEntityFilter(new String[] { CLight.NAME }, new String[0],
			new String[0], false);

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
	public void entityAdded(Entity entity, TreeNode parent, Scene scene) {

	}

	@Override
	public void entityRemoved(Entity entity, TreeNode parent, Scene scene) {

	}

	@Override
	public void entityMoved(Entity entity, TreeNode oldParent, TreeNode newParent, Scene scene) {

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

		renderer.popModel();
	}

	@Override
	public void postUpdate() {

	}

	@Override
	public SimpleEntityFilter getUpdateFilter() {
		return s_filter;
	}

	@Override
	public SimpleEntityFilter getEntityEventFilter() {
		return s_filter;
	}
}