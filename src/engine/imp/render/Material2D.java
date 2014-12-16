package engine.imp.render;

import glcommon.Color;
import gltools.texture.Texture2D;

/**
 * Defines a rendering material.
 */
public class Material2D {
	private Texture2D m_texture;
	private Texture2D m_normal;
	private Color m_color;
	private boolean m_lighted = true;

	public Material2D() {
	}

	public Material2D(boolean lighted) {
		m_lighted = lighted;
	}

	public Material2D(Color color, boolean lighted) {
		m_color = color;
		m_lighted = lighted;
	}

	public Material2D(Texture2D texture, boolean lighted) {
		m_texture = texture;
		m_lighted = lighted;
	}

	public Material2D(Texture2D texture, Texture2D normal, boolean lighted) {
		m_texture = texture;
		m_normal = normal;
		m_lighted = lighted;
	}

	public Material2D(Texture2D texture, Texture2D normal, Color color, boolean lighted) {
		m_texture = texture;
		m_normal = normal;
		m_color = color;
		m_lighted = lighted;
	}

	/**
	 * @return the Texture2D
	 */
	public Texture2D getTexture() {
		return m_texture;
	}

	/**
	 * Sets the Texture2D.
	 * 
	 * @param texture
	 */
	public void setTexture(Texture2D texture) {
		m_texture = texture;
	}

	/**
	 * @return the normal map
	 */
	public Texture2D getNormalTexture() {
		return m_normal;
	}

	/**
	 * Sets the normal map.
	 * 
	 * @param normal
	 */
	public void setNormalTexture(Texture2D normal) {
		m_normal = normal;
	}

	/**
	 * @return the color tint
	 */
	public Color getColor() {
		return m_color;
	}

	/**
	 * Sets the color tint.
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		m_color = color;
	}

	/**
	 * @return whether lights affect this Material2D.
	 */
	public boolean isLighted() {
		return m_lighted;
	}

	/**
	 * Sets whether lights affect this Material2D.
	 * 
	 * @param lighted
	 */
	public void setLighted(boolean lighted) {
		m_lighted = lighted;
	}
}
