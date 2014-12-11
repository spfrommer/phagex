package engine.imp.render;

import commons.Transform2f;

import engine.core.Entity;
import engine.core.EntitySystem;
import engine.core.Scene;
import engine.core.SimpleEntityFilter;
import engine.core.TreeNode;
import engine.core.script.XScript;
import glcommon.vector.Vector3f;
import glextra.renderer.Light;
import glextra.renderer.Light.PointLight;
import glextra.renderer.Renderer2D;

/**
 * Draws Lights to a RenderSystem.
 */
public class LightingSystem implements EntitySystem {
	private static final SimpleEntityFilter s_filter = new SimpleEntityFilter(new String[] { CLight.NAME },
			new String[0], new String[0], false);

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
	public void update(float time, Scene scene) {

	}

	@Override
	public void updateEntity(Entity entity, Scene scene) {
		Renderer2D renderer = m_render.getRenderer();

		Light light = ((CLight) entity.components().get(CLight.NAME)).getLight();
		Transform2f transform = scene.getWorldTransform(entity);

		if (light instanceof PointLight) {
			((PointLight) light).setPosition(new Vector3f(transform.getTranslation().getX(), transform.getTranslation()
					.getY(), 1f));
		}

		renderer.renderLight(light);
	}

	@Override
	public void postUpdate(Scene scene) {

	}

	@Override
	public SimpleEntityFilter getUpdateFilter() {
		return s_filter;
	}

	@Override
	public SimpleEntityFilter getEntityEventFilter() {
		return s_filter;
	}

	@Override
	public void scriptAdded(Entity entity, XScript script, Scene scene) {
	}
}