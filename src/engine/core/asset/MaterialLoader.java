package engine.core.asset;

import java.io.IOException;

import commons.GLResourceLocator;
import commons.Resource;

import engine.imp.render.Material2D;
import engine.imp.render.RenderingException;
import gltools.gl.GL;
import gltools.texture.Texture2D;
import gltools.texture.TextureFactory;
import gltools.texture.TextureWrapMode;

/**
 * Loads materials. If there is no MaterialParams supplied, the Material2D will be lighted.
 */
public class MaterialLoader implements AssetLoader<Material2D> {
	private GL m_gl;

	public MaterialLoader(GL gl) {
		m_gl = gl;
	}

	@Override
	public Material2D load(Resource resource, Object[] params) {
		Material2D mat = new Material2D();
		mat.setTexture(createTexture(resource));

		if (params.length == 1) {
			MaterialParams matParams = (MaterialParams) params[0];
			mat.setNormalTexture(createTexture(matParams.normalMap));
			mat.setColor(matParams.color);
			mat.setLighted(matParams.isLighted);
		} else {
			mat.setLighted(true);
		}

		return mat;
	}

	/**
	 * Makes a Texture2D from a Resource.
	 * 
	 * @param resource
	 * @return
	 */
	public Texture2D createTexture(Resource resource) {
		try {
			Texture2D texture = TextureFactory.s_loadTexture(m_gl, resource.getResource(), new GLResourceLocator(
					resource.getLocator()));
			texture.bind(m_gl);
			texture.setTWrapMode(TextureWrapMode.REPEAT);
			texture.setSWrapMode(TextureWrapMode.REPEAT);
			texture.loadParams(m_gl);
			texture.unbind(m_gl);
			return texture;
		} catch (IOException e) {
			throw new RenderingException("Material could not be created: " + resource.getResource(), e);
		}
	}
}