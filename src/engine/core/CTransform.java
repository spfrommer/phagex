package engine.core;

import commons.Logger;
import commons.Transform2f;
import commons.matrix.Vector2f;

import engine.core.exceptions.ComponentException;

/**
 * A Component which holds an Entity's Transform2f.
 */
public class CTransform implements Component {
	public static final String NAME = "transform";
	public static final String TRANSLATION = "translation";
	public static final String ROTATION = "rotation";
	public static final String SCALE = "scale";
	private static final String[] IDENTIFIERS = new String[] { TRANSLATION, ROTATION, SCALE };

	private Entity m_entity;
	private Transform2f m_transform;

	private boolean m_warnings = false;

	/**
	 * Initializes a CTransform with only a transform.
	 * 
	 * @param transform
	 */
	public CTransform(Transform2f transform) {
		m_transform = transform;
		m_warnings = true;
	}

	/**
	 * Gives a CTransform a transform and a required Entity.
	 * 
	 * @param entity
	 * @param transform
	 */
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

		Transform2f oldTrans = m_transform;
		m_transform = transform;
		for (EntityListener listener : m_entity.getListeners())
			listener.transformSet(m_entity, oldTrans, transform, m_entity.getScene());
	}

	/**
	 * Sets the Transform2f without notifying the listeners. This shouldn't be called.
	 * 
	 * @param transform
	 */
	public void quietSetTransform(Transform2f transform) {
		if (m_warnings)
			Logger.instance().warn("Setting transform on a unbound CTransform");

		if (transform == null)
			throw new ComponentException("Cannot set a null transform!");

		m_transform = transform;
	}

	/**
	 * Adds a translation to the current translation.
	 * 
	 * @param translate
	 */
	public void translate(Vector2f translate) {
		if (translate == null)
			throw new ComponentException("Cannot add a null translation!");
		translate(translate.getX(), translate.getY());
	}

	/**
	 * Adds a translation to the current translation.
	 * 
	 * @param x
	 * @param y
	 */
	public void translate(float x, float y) {
		Transform2f newTrans = new Transform2f(m_transform);
		newTrans.setTranslation(m_transform.getTranslation().add(new Vector2f(x, y)).toVector2f());
		setTransform(newTrans);
	}

	/**
	 * Adds a rotation to the current rotation.
	 * 
	 * @param rotate
	 */
	public void rotate(float rotate) {
		Transform2f newTrans = new Transform2f(m_transform);
		newTrans.setRotation(m_transform.getRotation() + rotate);
		setTransform(newTrans);
	}

	/**
	 * Multiplies a scale to the current scale.
	 * 
	 * @param scale
	 */
	public void scale(Vector2f scale) {
		if (scale == null)
			throw new ComponentException("Cannot multiply by a null scale!");

		scale(scale.getX(), scale.getY());
	}

	/**
	 * Multiplies a scale to the current scale.
	 * 
	 * @param x
	 * @param y
	 */
	public void scale(float x, float y) {
		Transform2f newTrans = new Transform2f(m_transform);
		Vector2f oldScale = m_transform.getScale();
		Vector2f newScale = new Vector2f(x * oldScale.getX(), y * oldScale.getY());
		newTrans.setScale(newScale);
		setTransform(newTrans);
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

		if (identifier == null)
			throw new ComponentException("Cannot set transform data with a null identifier!");
		if (data == null)
			throw new ComponentException("Cannot set null data for identifier: " + identifier);

		Transform2f newTransform = new Transform2f(m_transform);
		if (identifier.equals(TRANSLATION)) {
			newTransform.setTranslation((Vector2f) data);
		} else if (identifier.equals(ROTATION)) {
			newTransform.setRotation((Float) data);
		} else if (identifier.equals(SCALE)) {
			newTransform.setScale((Vector2f) data);
		} else {
			throw new ComponentException("No data for identifier: " + identifier);
		}

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

	@Override
	public String toString() {
		return m_transform.toString();
	}
}
