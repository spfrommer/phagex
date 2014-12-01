package engine.imp.render;

import commons.Transform2f;

import engine.core.Entity;
import engine.core.EntityFilter;
import engine.core.EntitySystem;
import glextra.renderer.LWJGLRenderer2D;
import glextra.renderer.Renderer2D;
import gltools.display.Display;
import gltools.display.Window;
import gltools.gl.lwjgl.glfw.GLFWWindow;

/**
 * Renders the Entities with a CRender to a Display.
 */
public class RenderingSystem implements EntitySystem {
	public static final float DRAW_WIDTH = 1f;

	private static final EntityFilter s_filter = new EntityFilter(new String[] { CRender.NAME }, new String[0],
			new String[0]);

	private Display m_display;
	private Renderer2D m_renderer;

	/**
	 * @param width
	 *            the width of the screen's coordinate system, in meters
	 * @param height
	 *            the hieght of the screen's coordinate system, in meters
	 */
	public RenderingSystem(float width, float height) {
		Window window = new GLFWWindow();
		window.setResizable(true);
		window.init();
		window.getGL().makeCurrent();
		window.setSize(800, 800);

		float widthHalf = width * 0.5f;
		float heightHalf = height * 0.5f;
		m_renderer = new LWJGLRenderer2D();
		m_renderer.init(widthHalf, widthHalf, heightHalf, heightHalf, window);

		m_display = window;
	}

	/**
	 * @return the Display to draw on
	 */
	protected Display getDisplay() {
		return m_display;
	}

	/**
	 * @return the renderer
	 */
	protected Renderer2D getRenderer() {
		return m_renderer;
	}

	@Override
	public void update(float time) {
		m_renderer.clear();
		m_renderer.startLighted();
		m_renderer.startGeometry();
	}

	@Override
	public void updateEntity(Entity entity) {
		Transform2f transform = entity.getCTransform().getTransform();
		CRender render = (CRender) entity.components().get(CRender.NAME);

		m_renderer.setMaterial(render.getMaterial());

		m_renderer.pushModel();
		m_renderer.translate(transform.getTranslation().getX(), transform.getTranslation().getY());
		m_renderer.rotate(transform.getRotation());
		m_renderer.scale(transform.getScale().getX(), transform.getScale().getY());

		m_renderer.fillRect(0, 0, DRAW_WIDTH, DRAW_WIDTH);

		m_renderer.popModel();
	}

	@Override
	public void postUpdate() {
		m_renderer.finishGeometry();
		m_renderer.finishLighted();
		m_renderer.doLightingComputations();
		m_display.update();
	}

	@Override
	public EntityFilter getFilter() {
		return s_filter;
	}
}
