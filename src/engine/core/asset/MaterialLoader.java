package engine.core.asset;

import commons.Resource;

import engine.imp.render.Material2D;
import gltools.gl.GL;

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
		mat.setTexture(ResourceFactory.createTexture(m_gl, resource));

		if (params.length == 1) {
			MaterialParams matParams = (MaterialParams) params[0];
			if (matParams.normalMap != null)
				mat.setNormalTexture(ResourceFactory.createTexture(m_gl, matParams.normalMap));
			if (matParams.color != null)
				mat.setColor(matParams.color);
			mat.setLighted(matParams.isLighted);
		} else {
			mat.setLighted(true);
		}

		return mat;
	}
}