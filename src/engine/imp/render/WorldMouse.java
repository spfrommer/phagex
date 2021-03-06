package engine.imp.render;

import engine.core.Entity;
import gltools.display.Display;
import gltools.input.Mouse;

import commons.matrix.Vector2f;

/**
 * A Mouse as defined in world coordinates. Adds the methods getWorldX() and getWorldY().
 */
public class WorldMouse {
	private Display m_display;
	private Mouse m_mouse;
	private RenderingSystem m_render;

	public WorldMouse(Display display, RenderingSystem render) {
		m_display = display;
		m_mouse = display.getMouse();
		m_render = render;
	}

	/**
	 * @return the world x on the playfield (1f)
	 */
	public float getWorldX() {
		Entity camera = m_render.getCamera();
		if (camera == null)
			return m_mouse.getX();
		Vector2f translate = camera.getScene().getWorldTransform(camera).getTranslation();
		float scale = ((CCamera) camera.components().get(CCamera.NAME)).getScale();

		float mx = (m_mouse.getX() / (float) m_display.getWidth()) * m_render.getWidth() - m_render.getWidth() / 2;
		mx /= scale;
		mx += translate.getX();

		return mx;
	}

	/**
	 * @return the world y on the playfield (1f)
	 */
	public float getWorldY() {
		Entity camera = m_render.getCamera();
		if (camera == null)
			return m_mouse.getY();
		Vector2f translate = camera.getScene().getWorldTransform(camera).getTranslation();
		float scale = ((CCamera) camera.components().get(CCamera.NAME)).getScale();

		float my = (m_mouse.getY() / (float) m_display.getHeight()) * m_render.getHeight() - m_render.getHeight() / 2;
		my /= scale;
		my += translate.getY();

		return my;
	}

	/**
	 * @return the core Mouse
	 */
	public Mouse getMouse() {
		return m_mouse;
	}
}
