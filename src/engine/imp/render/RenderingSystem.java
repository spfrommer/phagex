package engine.imp.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import commons.GLResourceLocator;
import commons.Resource;
import commons.ResourceLocator.ClasspathResourceLocator;
import commons.Transform2f;

import engine.core.Entity;
import engine.core.EntitySystem;
import engine.core.Scene;
import engine.core.SimpleEntityFilter;
import engine.core.TreeNode;
import engine.core.asset.AssetManager;
import engine.core.script.XScript;
import engine.core.script.XScriptObject;
import glcommon.Color;
import glextra.material.Material;
import glextra.material.MaterialXMLLoader;
import glextra.renderer.LWJGLRenderer2D;
import glextra.renderer.Renderer2D;
import gltools.display.Display;
import gltools.display.Window;
import gltools.gl.GL;
import gltools.gl.lwjgl.glfw.GLFWWindow;
import gltools.input.Keyboard;
import gltools.input.Mouse;
import gltools.texture.Texture2D;

/**
 * Renders the Entities with a CRender to a Display. Will also call mouse and keyboard events on the scripts.
 */
public class RenderingSystem implements EntitySystem {
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

	private WorldMouse m_mouse;

	private float m_width;
	private float m_height;

	public static final float DRAW_WIDTH = 1f;
	public static final float HALF_DRAW_WIDTH = 0.5f * DRAW_WIDTH;

	public static final String DEFAULT_MATERIAL_LOC = "Materials/M2D/2d.mat";
	public static final String MATERIAL_DIFFUSE_TEXTURE = "materialDiffuseTexture";
	public static final String MATERIAL_NORMAL_MAP = "materialNormalMap";
	public static final String MATERIAL_DIFFUSE_COLOR = "materialDiffuseColor";
	public static final String USE_LIGHTING = "useLighting";

	private HashMap<MaterialKey, Material> m_materials = new HashMap<MaterialKey, Material>();

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
		m_width = width;
		m_height = height;
		m_mouse = new WorldMouse(m_display, this);

		initMaterials(window.getGL());
	}

	private void initMaterials(GL gl) {
		Resource deferred = new Resource(new ClasspathResourceLocator(), DEFAULT_MATERIAL_LOC);

		Material lighted = createDeferred(deferred);
		lighted.setBoolean(USE_LIGHTING, true);
		m_materials.put(new MaterialKey(false, false, true), lighted);

		Material unlighted = createDeferred(deferred);
		unlighted.setBoolean(USE_LIGHTING, false);
		m_materials.put(new MaterialKey(false, false, false), unlighted);

		Material lColored = createDeferred(deferred);
		lColored.setBoolean(USE_LIGHTING, true);
		lColored.setColor(MATERIAL_DIFFUSE_COLOR, new Color(1, 1, 1));
		m_materials.put(new MaterialKey(true, false, true), lColored);

		Material uColored = createDeferred(deferred);
		uColored.setBoolean(USE_LIGHTING, false);
		uColored.setColor(MATERIAL_DIFFUSE_COLOR, new Color(1, 1, 1));
		m_materials.put(new MaterialKey(true, false, false), uColored);

		Texture2D blank = new Texture2D();
		blank.setWidth(1);
		blank.setHeight(1);
		blank.bind(gl, 0);
		blank.load(gl);
		blank.unbind(gl);

		Material lNormaled = createDeferred(deferred);
		lNormaled.setBoolean(USE_LIGHTING, true);
		lNormaled.setTexture2D(MATERIAL_NORMAL_MAP, blank);
		m_materials.put(new MaterialKey(false, true, true), lNormaled);

		Material uNormaled = createDeferred(deferred);
		uNormaled.setBoolean(USE_LIGHTING, false);
		uNormaled.setTexture2D(MATERIAL_NORMAL_MAP, blank);
		m_materials.put(new MaterialKey(false, true, false), uNormaled);

		Material lcNormaled = createDeferred(deferred);
		lcNormaled.setBoolean(USE_LIGHTING, true);
		lcNormaled.setTexture2D(MATERIAL_NORMAL_MAP, blank);
		lcNormaled.setColor(MATERIAL_DIFFUSE_COLOR, new Color(1, 1, 1));
		m_materials.put(new MaterialKey(true, true, true), lcNormaled);

		Material ucNormaled = createDeferred(deferred);
		lcNormaled.setBoolean(USE_LIGHTING, false);
		lcNormaled.setTexture2D(MATERIAL_NORMAL_MAP, blank);
		lcNormaled.setColor(MATERIAL_DIFFUSE_COLOR, new Color(1, 1, 1));
		m_materials.put(new MaterialKey(true, true, false), ucNormaled);
	}

	/**
	 * Makes a deferred Material.
	 * 
	 * @return
	 */
	private Material createDeferred(Resource resource) {
		try {
			return (MaterialXMLLoader.s_load(m_renderer.getGL(), resource.getResource(),
					new GLResourceLocator(resource.getLocator()))).get(0);
		} catch (Exception e) {
			throw new RenderingException("Deferred could not be created: " + DEFAULT_MATERIAL_LOC, e);
		}
	}

	/**
	 * TODO: hide this
	 * 
	 * @return the Display to draw on
	 */
	public Display getDisplay() {
		return m_display;
	}

	/**
	 * TODO: hide this
	 * 
	 * @return the renderer
	 */
	public Renderer2D getRenderer() {
		return m_renderer;
	}

	/**
	 * @return the current camera
	 */
	protected Entity getCamera() {
		return m_currentCam;
	}

	/**
	 * @return the width of the frame in units, not pixels
	 */
	protected float getWidth() {
		return m_width;
	}

	/**
	 * @return the height of the frame in units, not pixels
	 */
	protected float getHeight() {
		return m_height;
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
	public void update(Scene scene, float time) {
		m_renderer.clear();
		m_renderer.startLighted();
		m_renderer.startGeometry();
	}

	@Override
	public void updateEntity(Entity entity, Scene scene, float time) {
		CRender render = (CRender) entity.components().get(CRender.NAME);
		int layer = render.getLayer();
		if (!m_entityLayers.containsKey(layer))
			m_entityLayers.put(layer, new ArrayList<Entity>());

		m_entityLayers.get(layer).add(entity);
	}

	@Override
	public void postUpdate(Scene scene) {
		List<Integer> orderedLayers = new ArrayList<Integer>(m_entityLayers.keySet());
		Collections.sort(orderedLayers);

		for (Integer layer : orderedLayers) {
			List<Entity> entities = m_entityLayers.get(layer);
			for (Entity e : entities) {
				Transform2f transform = scene.getWorldTransform(e);
				CRender crender = (CRender) e.components().get(CRender.NAME);
				if (!crender.isVisible())
					continue;

				Material2D mat2d = crender.getMaterial();
				Texture2D texture = mat2d.getTexture();
				Texture2D normal = mat2d.getNormalTexture();
				Color color = mat2d.getColor();
				boolean isLighted = mat2d.isLighted();
				MaterialKey key = new MaterialKey(color != null, normal != null, isLighted);
				Material mat = m_materials.get(key);

				if (texture != null)
					mat.setTexture2D(MATERIAL_DIFFUSE_TEXTURE, texture);
				if (normal != null)
					mat.setTexture2D(MATERIAL_NORMAL_MAP, normal);
				if (color != null)
					mat.setColor(MATERIAL_DIFFUSE_COLOR, color);

				m_renderer.setMaterial(mat);

				m_renderer.pushModel();

				if (m_currentCam != null) {
					CCamera cam = (CCamera) m_currentCam.components().get(CCamera.NAME);
					Transform2f camTrans = scene.getWorldTransform(m_currentCam);
					float depth = crender.getDepth();

					float transX = (transform.getTranslation().getX() - camTrans.getTranslation().getX()) / depth;
					float transY = (transform.getTranslation().getY() - camTrans.getTranslation().getY()) / depth;

					m_renderer.scale(cam.getScale(), cam.getScale());

					m_renderer.translate(transX, transY);
					m_renderer.scale(1f / depth, 1f / depth);
					m_renderer.rotate(transform.getRotation());
					m_renderer.scale(transform.getScale().getX(), transform.getScale().getY());
				} else {
					m_renderer.translate(transform.getTranslation().getX(), transform.getTranslation().getY());
					m_renderer.rotate(transform.getRotation());
					m_renderer.scale(transform.getScale().getX(), transform.getScale().getY());
				}

				m_renderer.fillRect(-HALF_DRAW_WIDTH, -HALF_DRAW_WIDTH, DRAW_WIDTH, DRAW_WIDTH, crender.getRepeatX(),
						crender.getRepeatY());

				m_renderer.popModel();
			}
			entities.clear();
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
		script.addScriptObject(new XScriptObject("assets", AssetManager.instance()));
		script.addScriptObject(new XScriptObject("mouse", m_mouse));
		script.addScriptObject(new XScriptObject("keyboard", getKeyboard()));
	}

	private class MaterialKey {
		boolean isColored;
		boolean isNormaled;
		boolean isLighted;

		public MaterialKey(boolean isColored, boolean isNormaled, boolean isLighted) {
			this.isColored = isColored;
			this.isNormaled = isNormaled;
			this.isLighted = isLighted;
		}

		@Override
		public boolean equals(Object object) {
			if (!(object instanceof MaterialKey))
				return false;
			MaterialKey key = (MaterialKey) object;
			boolean equals = isColored == key.isColored && isNormaled == key.isNormaled && isLighted == key.isLighted;
			return equals;
		}

		@Override
		public int hashCode() {
			return Objects.hash(isColored, isNormaled, isLighted);
		}

		@Override
		public String toString() {
			return "Colored: " + isColored + ", Normaled: " + isNormaled + ", Lighted: " + isLighted;
		}
	}
}
