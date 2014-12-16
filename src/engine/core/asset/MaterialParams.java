package engine.core.asset;

import glcommon.Color;

import commons.Resource;

/**
 * Parameters for a Material.
 */
public class MaterialParams {
	public Resource normalMap;
	public Color color;
	public boolean isLighted;

	public MaterialParams(Resource normalMap, boolean isLighted) {
		this.normalMap = normalMap;
		this.isLighted = isLighted;
	}

	public MaterialParams(Color color, boolean isLighted) {
		this.color = color;
		this.isLighted = isLighted;
	}

	public MaterialParams(Resource normalMap, Color color, boolean isLighted) {
		this.normalMap = normalMap;
		this.color = color;
		this.isLighted = isLighted;
	}
}
