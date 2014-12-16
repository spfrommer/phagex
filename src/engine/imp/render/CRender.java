package engine.imp.render;

import engine.core.Component;
import engine.core.ComponentBuilder;
import engine.core.exceptions.ComponentException;

/**
 * Contains the data for the RenderSystem.
 */
public class CRender implements Component {
	public static final String NAME = "render";
	public static final String MATERIAL = "material";
	public static final String LAYER = "layer";
	public static final String DEPTH = "depth";
	public static final String REPEAT_X = "repeatX";
	public static final String REPEAT_Y = "repeatY";
	public static final String IS_VISIBLE = "isVisible";
	private static final String[] IDENTIFIERS = new String[] { MATERIAL, LAYER, DEPTH, REPEAT_X, REPEAT_Y, IS_VISIBLE };

	private Material2D m_material;
	private float m_depth;
	private int m_layer;
	private float m_repeatX = 1;
	private float m_repeatY = 1;
	private boolean m_isVisible = true;

	/**
	 * Creates a CRender with the given Material2D, a layer of 0, and a parallax depth of 1.
	 * 
	 * @param material
	 */
	public CRender(Material2D material) {
		if (material == null)
			throw new RenderingException("Cannot create with null Material!");
		m_material = material;
		m_depth = 1f;
		m_layer = 0;
	}

	/**
	 * Creates a CRender with the given Material 2Dand layer, with a parallax depth of 1.
	 * 
	 * @param material
	 * @param layer
	 */
	public CRender(Material2D material, int layer) {
		if (material == null)
			throw new RenderingException("Cannot create with null Material!");
		if (layer < 0)
			throw new RenderingException("Layer must be greater or equal to zero!");
		m_material = material;
		m_layer = layer;
	}

	/**
	 * Constructs a CRender from the material, parallax depth, and layer.
	 * 
	 * @param material
	 * @param layer
	 *            the draw order - 0 is background layer. Elements on the same layer will be drawn in a random order.
	 * @param depth
	 *            the depth of the paralax rendering; 1 is the normal plane
	 */
	public CRender(Material2D material, int layer, float depth) {
		if (material == null)
			throw new RenderingException("Cannot create with null Material!");
		if (depth <= 0)
			throw new RenderingException("Depth must be greater than zero!");
		if (layer < 0)
			throw new RenderingException("Layer must be greater than or equal to zero!");
		m_material = material;
		m_depth = depth;
		m_layer = layer;
	}

	/**
	 * @return the Material
	 */
	public Material2D getMaterial() {
		return m_material;
	}

	/**
	 * Sets the Material.
	 * 
	 * @param material
	 */
	public void setMaterial(Material2D material) {
		if (material == null)
			throw new RenderingException("Cannot set a null Material2D!");
		m_material = material;
	}

	/**
	 * Returns the depth from the Camera. 1 is the normal plane.
	 * 
	 * @return the depth
	 */
	public float getDepth() {
		return m_depth;
	}

	/**
	 * Sets the depth from the Camera. 1 is the normal plane.
	 * 
	 * @param depth
	 */
	public void setDepth(float depth) {
		if (depth <= 0)
			throw new RenderingException("Depth must be greater than zero!");
		m_depth = depth;
	}

	/**
	 * @return the layer--0 is background layer, and objects on the same layer are drawn in a random order
	 */
	public int getLayer() {
		return m_layer;
	}

	/**
	 * Sets the layer--0 is background layer, and objects on the same layer are drawn in a random order.
	 * 
	 * @param layer
	 */
	public void setLayer(int layer) {
		if (layer <= 0)
			throw new RenderingException("Layer must be greater than zero!");
		m_layer = layer;
	}

	/**
	 * @return how often the texture should be repeated ("tiled") in the x direction. It will still fit into the alloted
	 *         scale.
	 */
	public float getRepeatX() {
		return m_repeatX;
	}

	/**
	 * Sets how often the texture should be repeated ("tiled") in the x direction. It will still fit into the alloted
	 * scale.
	 * 
	 * @param repeatX
	 * @return
	 */
	public void setRepeatX(float repeatX) {
		if (repeatX <= 0)
			throw new RenderingException("Repeat must be greater than zero!");
		m_repeatX = repeatX;
	}

	/**
	 * @return how often the texture should be repeated ("tiled") in the y direction. It will still fit into the alloted
	 *         scale.
	 */
	public float getRepeatY() {
		return m_repeatY;
	}

	/**
	 * Sets how often the texture should be repeated ("tiled") in the y direction. It will still fit into the alloted
	 * scale.
	 * 
	 * @param repeatY
	 * @return
	 */
	public void setRepeatY(float repeatY) {
		if (repeatY <= 0)
			throw new RenderingException("Repeat must be greater than zero!");
		m_repeatY = repeatY;
	}

	/**
	 * @return whether this Entity should be rendered
	 */
	public boolean isVisible() {
		return m_isVisible;
	}

	/**
	 * Sets whether this Entity should be rendered.
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		m_isVisible = visible;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String[] getIdentifiers() {
		return IDENTIFIERS;
	}

	@Override
	public Object getData(String identifier) {
		if (identifier == null)
			throw new ComponentException("Cannot get data for null identifier!");

		if (identifier.equals(MATERIAL))
			return m_material;
		if (identifier.equals(LAYER))
			return m_layer;
		if (identifier.equals(DEPTH))
			return m_depth;
		if (identifier.equals(REPEAT_X))
			return m_repeatX;
		if (identifier.equals(REPEAT_Y))
			return m_repeatY;
		if (identifier.equals(IS_VISIBLE))
			return m_isVisible;

		throw new ComponentException("No such identifier!");
	}

	@Override
	public void setData(String identifier, Object data) {
		if (identifier == null)
			throw new ComponentException("Cannot set data for null identifier!");
		if (data == null)
			throw new ComponentException("Cannot set null data for identifier: " + identifier);

		if (identifier.equals(MATERIAL)) {
			m_material = (Material2D) data;
		} else if (identifier.equals(LAYER)) {
			m_layer = (Integer) data;
		} else if (identifier.equals(DEPTH)) {
			m_depth = (Float) data;
		} else if (identifier.equals(REPEAT_X)) {
			m_repeatX = (Float) data;
		} else if (identifier.equals(REPEAT_Y)) {
			m_repeatY = (Float) data;
		} else if (identifier.equals(IS_VISIBLE)) {
			m_isVisible = (Boolean) data;
		} else {
			throw new ComponentException("No data for identifier: " + identifier);
		}
	}

	@Override
	public ComponentBuilder<CRender> getBuilder() {
		ComponentBuilder<CRender> builder = new ComponentBuilder<CRender>() {
			@Override
			public CRender build() {
				CRender newRender = new CRender(m_material, m_layer, m_depth);
				newRender.setRepeatX(m_repeatX);
				newRender.setRepeatY(m_repeatY);
				newRender.setVisible(m_isVisible);
				return newRender;
			}

			@Override
			public String getName() {
				return NAME;
			}
		};
		return builder;
	}
}
