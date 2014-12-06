package engine.imp.render;

import glcommon.Color;
import glextra.material.Material;
import glextra.material.MaterialXMLLoader;
import gltools.texture.Texture2D;
import gltools.texture.TextureFactory;

import java.io.IOException;

import commons.GLResourceLocator;
import commons.Resource;
import commons.ResourceLocator.ClasspathResourceLocator;

/**
 * A MaterialFactory bound to a RenderSystem for the Display.
 */
public class MaterialFactory {
	public static String MATERIAL_LOC = "Materials/M2D/2d.mat";

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
		Material mat = makeDeferred();

		mat.setTexture2D("materialDiffuseTexture", makeTexture(resource));
		mat.setBoolean("useLighting", false);

		return mat;
	}

	/**
	 * Creates a Material.
	 * 
	 * @param resource
	 * @return
	 */
	public Material createLighted(Resource resource) {
		Material mat = makeDeferred();

		mat.setTexture2D("materialDiffuseTexture", makeTexture(resource));
		mat.setBoolean("useLighting", true);

		return mat;
	}

	/**
	 * Creates a white Material.
	 * 
	 * @return
	 */
	public Material create() {
		Material mat = makeDeferred();
		mat.setColor("materialDiffuseColor", new Color(1, 1, 1));
		mat.setBoolean("useLighting", false);
		return mat;
	}

	/**
	 * Makes a deferred Material.
	 * 
	 * @return
	 */
	private Material makeDeferred() {
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
	private Texture2D makeTexture(Resource resource) {
		try {
			return TextureFactory.s_loadTexture(m_system.getRenderer().getGL(), resource.getResource(),
					new GLResourceLocator(resource.getLocator()));
		} catch (IOException e) {
			throw new RenderingException("Material could not be created: " + resource.getResource(), e);
		}
	}
}
