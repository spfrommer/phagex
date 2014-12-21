package engine.core.asset;

import java.io.IOException;

import commons.GLResourceLocator;
import commons.Resource;

import engine.core.exceptions.AssetException;
import engine.imp.render.Material2D;
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
		mat.setTexture(createTexture(m_gl, resource));

		if (params.length == 1) {
			MaterialParams matParams = (MaterialParams) params[0];
			if (matParams.normalMap != null)
				mat.setNormalTexture(createTexture(m_gl, matParams.normalMap));
			if (matParams.color != null)
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
	 * @param gl
	 * @param resource
	 * @return
	 */
	public static Texture2D createTexture(GL gl, Resource resource) {
		try {
			Texture2D texture = TextureFactory.s_loadTexture(gl, resource.getResource(),
					new GLResourceLocator(resource.getLocator()));
			texture.bind(gl);
			texture.setTWrapMode(TextureWrapMode.REPEAT);
			texture.setSWrapMode(TextureWrapMode.REPEAT);
			texture.loadParams(gl);
			texture.unbind(gl);
			return texture;
		} catch (IOException e) {
			throw new AssetException("Material could not be created: " + resource.getResource(), e);
		}
	}
}