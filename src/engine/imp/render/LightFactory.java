package engine.imp.render;

import glcommon.Color;
import glextra.renderer.Light;
import glextra.renderer.Light.PointLight;

import commons.matrix.Vector3f;

/**
 * Creates Lights.
 */
public class LightFactory {
	/**
	 * Returns a clone of the Light. Right now only PointLights are supported.
	 * 
	 * @param light
	 * @return
	 */
	public static Light clone(Light light) {
		if (light instanceof PointLight) {
			PointLight point = (PointLight) light;
			PointLight clone = new PointLight(new glcommon.vector.Vector3f(point.getPosition()),
					new glcommon.vector.Vector3f(point.getAttenuation()), new Color(point.getDiffuseColor()),
					new Color(point.getAmbientColor()));
			return clone;
		} else {
			throw new RenderingException("Cannot clone Light: " + light);
		}
	}

	/**
	 * Creates a simple diffuse PointLight.
	 * 
	 * @param pos
	 * @param attenuation
	 * @param diffuse
	 * @return
	 */
	public static Light createDiffusePoint(Vector3f pos, Vector3f attenuation, Color diffuse) {
		Light light = new PointLight(toGLVector(pos), toGLVector(attenuation), diffuse, new Color(0f, 0f, 0f));
		return light;
	}

	/**
	 * Creates an ambient Light.
	 * 
	 * @param color
	 * @return
	 */
	public static Light createAmbient(Color color) {
		Light light = new PointLight(new glcommon.vector.Vector3f(0f, 0f, 1f),
				new glcommon.vector.Vector3f(1f, 0f, 0f), new Color(0f, 0f, 0f), color);
		return light;
	}

	private static glcommon.vector.Vector3f toGLVector(Vector3f vector) {
		return new glcommon.vector.Vector3f(vector.getX(), vector.getY(), vector.getZ());
	}
}
