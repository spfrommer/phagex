package engine.imp.render;

import engine.core.Component;
import engine.core.ComponentBuilder;
import engine.core.exceptions.ComponentException;
import glextra.material.Material;

/**
 * Contains the data for the RenderSystem.
 */
public class CRender implements Component {
	public static final String NAME = "render";
	public static final String MATERIAL = "material";
	public static final String DEPTH = "depth";
	public static final String LAYER = "layer";
	private static final String[] IDENTIFIERS = new String[] { MATERIAL, DEPTH, LAYER };

	private Material m_material;
	private float m_depth;
	private int m_layer;

	/**
	 * Creates a CRender with the given Material, a layer of 0, and a parallax depth of 1.
	 * 
	 * @param material
	 */
	public CRender(Material material) {
		if (material == null)
			throw new RenderingException("Cannot create with null Material!");
		m_material = material;
		m_depth = 1f;
		m_layer = 0;
	}

	/**
	 * Creates a CRender with the given Material and layer, with a parallax depth of 1.
	 * 
	 * @param material
	 * @param layer
	 */
	public CRender(Material material, int layer) {
		if (material == null)
			throw new RenderingException("Cannot create with null Material!");
		if (layer < 0)
			throw new RenderingException("Layer must be greater or equal to zero!");
		m_material = material;
		m_layer = layer;
	}

	/**
	 * Creates a CRender with a given Material and parallax depth, with a layer of 0.
	 * 
	 * @param material
	 * @param depth
	 */
	public CRender(Material material, float depth) {
		if (material == null)
			throw new RenderingException("Cannot create with null Material!");
		if (depth <= 0)
			throw new RenderingException("Depth must be greater than zero!");
		m_material = material;
		m_depth = depth;
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
	public CRender(Material material, int layer, float depth) {
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
	public Material getMaterial() {
		return m_material;
	}

	/**
	 * Sets the Material.
	 * 
	 * @param material
	 */
	public void setMaterial(Material material) {
		if (material == null)
			throw new RenderingException("Cannot set a null Material!");
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
		if (identifier.equals(DEPTH))
			return m_depth;

		throw new ComponentException("No such identifier!");
	}

	@Override
	public void setData(String identifier, Object data) {
		if (identifier == null)
			throw new ComponentException("Cannot set data for null identifier!");
		if (data == null)
			throw new ComponentException("Cannot set null data for identifier: " + identifier);

		if (identifier.equals(MATERIAL)) {
			m_material = (Material) data;
		} else if (identifier.equals(DEPTH)) {
			m_depth = (Float) data;
		} else {
			throw new ComponentException("No data for identifier: " + identifier);
		}
	}

	@Override
	public ComponentBuilder<CRender> getBuilder() {
		ComponentBuilder<CRender> builder = new ComponentBuilder<CRender>() {
			@Override
			public CRender build() {
				return new CRender(m_material, m_layer, m_depth);
			}

			@Override
			public String getName() {
				return NAME;
			}
		};
		return builder;
	}
}
