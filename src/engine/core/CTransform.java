package engine.core;

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

	public CTransform(Entity entity, Transform2f transform) {
		m_entity = entity;
		m_transform = transform;
	}

	/**
	 * Returns the raw Transform2f data. Do not modify this - call Entity.setTransform() or CTransform.setTransform()
	 * instead.
	 * 
	 * @return
	 */
	public Transform2f getTransform() {
		return m_transform;
	}

	/**
	 * Sets the Transform2f.
	 * 
	 * @param transform
	 */
	public void setTransform(Transform2f transform) {
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
		Transform2f newTransform = new Transform2f(m_transform);
		if (identifier.equals(TRANSLATION))
			newTransform.setTranslation((Vector2f) data);
		if (identifier.equals(ROTATION))
			newTransform.setRotation((Float) data);
		if (identifier.equals(SCALE))
			newTransform.setScale((Vector2f) data);

		setTransform(newTransform);
	}
}
