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
	public static final String MATERIAL = "render_material";
	public static final String DEPTH = "render_depth";
	public static final String[] IDENTIFIERS = new String[] { MATERIAL, DEPTH };

	private Material m_material;
	private float m_depth;

	/**
	 * Constructs a CRender from the material.
	 * 
	 * @param material
	 * @param depth
	 *            the depth of the paralax rendering; 1 is the normal plane
	 */
	public CRender(Material material, float depth) {
		m_material = material;
		m_depth = depth;
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
			throw new RenderException("Cannot set a null Material!");
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
			throw new RenderException("Depth must be greater than zero!");
		m_depth = depth;
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
				return new CRender(m_material, m_depth);
			}

			@Override
			public String getName() {
				return NAME;
			}
		};
		return builder;
	}
}
