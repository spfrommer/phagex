package engine.imp.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import commons.Transform2f;

import engine.core.Entity;
import engine.core.EntitySystem;
import engine.core.Scene;
import engine.core.SimpleEntityFilter;
import engine.core.TreeNode;
import engine.core.script.XScript;
import engine.core.script.XScriptObject;
import glextra.renderer.LWJGLRenderer2D;
import glextra.renderer.Renderer2D;
import gltools.display.Display;
import gltools.display.Window;
import gltools.gl.lwjgl.glfw.GLFWWindow;
import gltools.input.Keyboard;
import gltools.input.Mouse;

/**
 * Renders the Entities with a CRender to a Display. Will also call mouse and keyboard events on the scripts.
 */
public class RenderingSystem implements EntitySystem {
	public static final float DRAW_WIDTH = 1f;
	public static final float HALF_DRAW_WIDTH = 0.5f * DRAW_WIDTH;

	private static final SimpleEntityFilter s_eventFilter = new SimpleEntityFilter(new String[] { CCamera.NAME },
			new String[0], new String[0], false);
	private static final SimpleEntityFilter s_updateFilter = new SimpleEntityFilter(new String[] { CRender.NAME },
			new String[0], new String[0], false);
	private HashMap<Integer, List<Entity>> m_entityLayers = new HashMap<Integer, List<Entity>>();

	// holds the cameras in each Scene
	private List<Entity> m_cameras = new ArrayList<Entity>();

	private Entity m_currentCam;

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

	protected void lookThroughCalled(CCamera camera) {
		if (m_currentCam != null) {
			CCamera currentCam = (CCamera) m_currentCam.components().get(CCamera.NAME);
			currentCam.setLookThrough(false);
		}

		for (Entity e : m_cameras) {
			if (e.components().get(CCamera.NAME) == camera) {
				m_currentCam = e;
				return;
			}
		}
	}

	@Override
	public void sceneChanged(Scene oldScene, Scene newScene) {
		m_cameras.clear();
		List<Entity> newAll = newScene.getEntitiesByFilter(s_eventFilter);

		for (Entity e : newAll) {
			CCamera camera = (CCamera) e.components().get(CCamera.NAME);
			camera.setRenderingSystem(this);
			if (camera.isLookedThrough()) {
				if (m_currentCam != null) {
					CCamera current = (CCamera) m_currentCam.components().get(CCamera.NAME);
					current.setLookThrough(false);
				}
				m_currentCam = e;
			}

			m_cameras.add(e);
		}
	}

	@Override
	public void entityAdded(Entity entity, TreeNode parent, Scene scene) {
		CCamera camera = (CCamera) entity.components().get(CCamera.NAME);
		camera.setRenderingSystem(this);

		if (scene.isCurrent()) {
			if (camera.isLookedThrough()) {
				CCamera current = (CCamera) m_currentCam.components().get(CCamera.NAME);
				current.setLookThrough(false);
				m_currentCam = entity;
			}

			m_cameras.add(entity);
		}
	}

	@Override
	public void entityRemoved(Entity entity, TreeNode parent, Scene scene) {
		if (scene.isCurrent()) {
			m_cameras.remove(entity);

			if (m_currentCam == entity) {
				Entity nextCam = m_cameras.get(0);
				CCamera next = (CCamera) nextCam.components().get(CCamera.NAME);
				next.setLookThrough(true);
				m_currentCam = nextCam;
			}
		}
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
		CRender render = (CRender) entity.components().get(CRender.NAME);
		int layer = render.getLayer();
		if (!m_entityLayers.containsKey(layer))
			m_entityLayers.put(layer, new ArrayList<Entity>());

		m_entityLayers.get(layer).add(entity);
	}

	@Override
	public void postUpdate(Scene scene) {
		if (m_currentCam != null) {
			CCamera cam = (CCamera) m_currentCam.components().get(CCamera.NAME);
			Transform2f transform = scene.getWorldTransform(m_currentCam);

			m_renderer.pushModel();

			m_renderer.scale(cam.getScale(), cam.getScale());

			m_renderer.translate(-transform.getTranslation().getX(), -transform.getTranslation().getY());
		}

		List<Integer> orderedLayers = new ArrayList<Integer>(m_entityLayers.keySet());
		Collections.sort(orderedLayers);

		for (Integer layer : orderedLayers) {
			List<Entity> entities = m_entityLayers.get(layer);
			for (Entity e : entities) {
				Transform2f transform = scene.getWorldTransform(e);
				m_renderer.setMaterial(((CRender) e.components().get(CRender.NAME)).getMaterial());

				m_renderer.pushModel();
				m_renderer.translate(transform.getTranslation().getX(), transform.getTranslation().getY());
				m_renderer.rotate(transform.getRotation());
				m_renderer.scale(transform.getScale().getX(), transform.getScale().getY());

				m_renderer.fillRect(-HALF_DRAW_WIDTH, -HALF_DRAW_WIDTH, DRAW_WIDTH, DRAW_WIDTH);

				m_renderer.popModel();
			}
			entities.clear();
		}

		if (m_currentCam != null) {
			m_renderer.popModel();
		}

		m_renderer.finishGeometry();
		m_renderer.finishLighted();
		m_renderer.doLightingComputations();
		m_display.update();
	}

	@Override
	public SimpleEntityFilter getUpdateFilter() {
		return s_updateFilter;
	}

	@Override
	public SimpleEntityFilter getEntityEventFilter() {
		return s_eventFilter;
	}

	@Override
	public void scriptAdded(Entity entity, XScript script, Scene scene) {
		script.addScriptObject(new XScriptObject("mouse", getMouse()));
		script.addScriptObject(new XScriptObject("keyboard", getKeyboard()));
	}
}
