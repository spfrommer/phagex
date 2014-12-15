package engine.imp.render;

import glcommon.Color;
import glextra.material.Material;
import glextra.material.MaterialXMLLoader;
import gltools.texture.Texture2D;
import gltools.texture.TextureFactory;
import gltools.texture.TextureWrapMode;

import java.io.IOException;

import commons.GLResourceLocator;
import commons.Resource;
import commons.ResourceLocator.ClasspathResourceLocator;

/**
 * A MaterialFactory bound to a RenderSystem for the Display.
 */
public class MaterialFactory {
	public static final String MATERIAL_LOC = "Materials/M2D/2d.mat";
	public static final String MATERIAL_DIFFUSE_TEXTURE = "materialDiffuseTexture";
	public static final String MATERIAL_DIFFUSE_COLOR = "materialDiffuseColor";
	public static final String USE_LIGHTING = "useLighting";

	private RenderingSystem m_system;
	private Resource m_deferred = new Resource(new ClasspathResourceLocator(), MATERIAL_LOC);

	/**
	 * Bind the factory to a RenderSystem.
	 * 
	 * @param system
	 */
	public MaterialFactory(RenderingSystem system) {
		m_system = system;
	}

	/**
	 * Creates a Material.
	 * 
	 * @param resource
	 * @return
	 */
	public Material createUnlighted(Resource resource) {
		Material mat = createDeferred();

		mat.setTexture2D(MATERIAL_DIFFUSE_TEXTURE, createTexture(resource));
		mat.setBoolean(USE_LIGHTING, false);

		return mat;
	}

	/**
	 * Creates a Material.
	 * 
	 * @param resource
	 * @return
	 */
	public Material createLighted(Resource resource) {
		Material mat = createDeferred();

		mat.setTexture2D(MATERIAL_DIFFUSE_TEXTURE, createTexture(resource));
		mat.setBoolean(USE_LIGHTING, true);

		return mat;
	}

	/**
	 * Creates an unlighted colored Material.
	 * 
	 * @param color
	 * @return
	 */
	public Material createUnlighted(Color color) {
		Material mat = createDeferred();
		mat.setColor(MATERIAL_DIFFUSE_COLOR, color);
		mat.setBoolean(USE_LIGHTING, false);
		return mat;
	}

	/**
	 * Creates an lighted colored Material.
	 * 
	 * @param color
	 * @return
	 */
	public Material createLighted(Color color) {
		Material mat = createDeferred();
		mat.setColor(MATERIAL_DIFFUSE_COLOR, color);
		mat.setBoolean(USE_LIGHTING, true);
		return mat;
	}

	/**
	 * Creates a white unlighted Material.
	 * 
	 * @return
	 */
	public Material create() {
		Material mat = createDeferred();
		mat.setColor(MATERIAL_DIFFUSE_COLOR, new Color(1, 1, 1));
		mat.setBoolean(USE_LIGHTING, false);
		return mat;
	}

	/**
	 * Makes a deferred Material.
	 * 
	 * @return
	 */
	private Material createDeferred() {
		try {
			return (MaterialXMLLoader.s_load(m_system.getDisplay().getGL(), m_deferred.getResource(),
					new GLResourceLocator(m_deferred.getLocator()))).get(0);
		} catch (Exception e) {
			throw new RenderingException("Deferred could not be created: " + MATERIAL_LOC, e);
		}
	}

	/**
	 * Makes a Texture2D from a Resource.
	 * 
	 * @param resource
	 * @return
	 */
	public Texture2D createTexture(Resource resource) {
		try {
			Texture2D texture = TextureFactory.s_loadTexture(m_system.getRenderer().getGL(), resource.getResource(),
					new GLResourceLocator(resource.getLocator()));
			texture.bind(m_system.getRenderer().getGL());
			texture.setTWrapMode(TextureWrapMode.REPEAT);
			texture.setSWrapMode(TextureWrapMode.REPEAT);
			texture.loadParams(m_system.getRenderer().getGL());
			texture.unbind(m_system.getRenderer().getGL());
			return texture;
		} catch (IOException e) {
			throw new RenderingException("Material could not be created: " + resource.getResource(), e);
		}
	}
}
