package engine.core;

import commons.Logger;
import commons.Transform2f;
import commons.matrix.Vector2f;

import engine.core.exceptions.ComponentException;

public class CTransform implements Component {
	public static final String NAME = "transform";
	public static final String TRANSLATION = "translation";
	public static final String ROTATION = "rotation";
	public static final String SCALE = "scale";
	public static final String[] IDENTIFIERS = new String[] { TRANSLATION, ROTATION, SCALE };

	private Entity m_entity;
	private Transform2f m_transform;

	private boolean m_warnings = false;

	public CTransform(Transform2f transform) {
		m_transform = transform;
		m_warnings = true;
	}

	public CTransform(Entity entity, Transform2f transform) {
		m_entity = entity;
		m_transform = transform;
		m_warnings = false;
	}

	/**
	 * Sets the Entity that this CTransform should notify.
	 * 
	 * @param entity
	 */
	public void setEntity(Entity entity) {
		m_entity = entity;
		m_warnings = false;
	}

	/**
	 * Returns the raw Transform2f data. Do not modify this - call Entity.setTransform() or CTransform.setTransform()
	 * instead.
	 * 
	 * @return
	 */
	public Transform2f getTransform() {
		if (m_warnings)
			Logger.instance().warn("Getting transform on a unbound CTransform");
		return m_transform;
	}

	/**
	 * Sets the Transform2f.
	 * 
	 * @param transform
	 */
	public void setTransform(Transform2f transform) {
		if (m_warnings)
			Logger.instance().warn("Setting transform on a unbound CTransform");

		if (transform == null)
			throw new ComponentException("Cannot set a null transform!");

		for (EntityListener listener : m_entity.getListeners())
			listener.transformSet(m_entity, m_transform, transform);
		m_transform = transform;
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
		if (m_warnings)
			Logger.instance().warn("Getting data on a unbound CTransform");

		if (identifier.equals(TRANSLATION))
			return m_transform.getTranslation();
		if (identifier.equals(ROTATION))
			return m_transform.getRotation();
		if (identifier.equals(SCALE))
			return m_transform.getScale();

		throw new ComponentException("No such identifier!");
	}

	@Override
	public void setData(String identifier, Object data) {
		if (m_warnings)
			Logger.instance().warn("Getting data on a unbound CTransform");

		Transform2f newTransform = new Transform2f(m_transform);
		if (identifier.equals(TRANSLATION))
			newTransform.setTranslation((Vector2f) data);
		if (identifier.equals(ROTATION))
			newTransform.setRotation((Float) data);
		if (identifier.equals(SCALE))
			newTransform.setScale((Vector2f) data);

		setTransform(newTransform);
	}

	@Override
	public ComponentBuilder<CTransform> getBuilder() {
		ComponentBuilder<CTransform> builder = new ComponentBuilder<CTransform>() {
			@Override
			public CTransform build() {
				return new CTransform(new Transform2f(m_transform));
			}

			@Override
			public String getName() {
				return NAME;
			}
		};
		return builder;
	}
}
