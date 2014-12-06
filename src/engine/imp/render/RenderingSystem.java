package engine.imp.render;

import java.util.Set;

import commons.Transform2f;

import engine.core.Entity;
import engine.core.EntitySystem;
import engine.core.Scene;
import engine.core.SimpleEntityFilter;
import engine.core.TreeNode;
import glextra.renderer.LWJGLRenderer2D;
import glextra.renderer.Renderer2D;
import gltools.display.Display;
import gltools.display.Window;
import gltools.gl.lwjgl.glfw.GLFWWindow;
import gltools.input.Keyboard;
import gltools.input.Mouse;

/**
 * Renders the Entities with a CRender to a Display.
 */
public class RenderingSystem implements EntitySystem {
	public static final float DRAW_WIDTH = 1f;
	public static final float HALF_DRAW_WIDTH = 0.5f * DRAW_WIDTH;

	private static final SimpleEntityFilter s_filter = new SimpleEntityFilter(new String[] { CRender.NAME },
			new String[0], new String[0], true);

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

	/**
	 * @return the Keyboard associated with the Display
	 */
	public Keyboard getKeyboard() {
		return m_display.getKeyboard();
	}

	/**
	 * @return the Mouse associated with the Display
	 */
	public Mouse getMouse() {
		return m_display.getMouse();
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
		m_renderer.clear();
		m_renderer.startLighted();
		m_renderer.startGeometry();
	}

	@Override
	public void updateEntity(Entity entity, Scene scene) {
		Transform2f transform = entity.getCTransform().getTransform();
		boolean hasCRender = entity.components().has(CRender.NAME);
		if (hasCRender) {
			CRender render = (CRender) entity.components().get(CRender.NAME);
			m_renderer.setMaterial(render.getMaterial());
		}

		m_renderer.pushModel();
		m_renderer.translate(transform.getTranslation().getX(), transform.getTranslation().getY());
		m_renderer.rotate(transform.getRotation());
		m_renderer.scale(transform.getScale().getX(), transform.getScale().getY());

		if (hasCRender)
			m_renderer.fillRect(-HALF_DRAW_WIDTH, -HALF_DRAW_WIDTH, DRAW_WIDTH, DRAW_WIDTH);

		Set<Entity> children = entity.tree().getChildren();
		for (Entity child : children) {
			updateEntity(child, scene);
		}

		m_renderer.popModel();
	}

	@Override
	public void postUpdate(Scene scene) {
		m_renderer.finishGeometry();
		m_renderer.finishLighted();
		m_renderer.doLightingComputations();
		m_display.update();
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
