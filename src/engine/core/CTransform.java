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

	private Entity m_entity;
	private Transform2f m_transform;

	private boolean m_translateChildren = true;
	private boolean m_rotateChildren = true;
	private boolean m_scaleChildren = true;

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
	protected Transform2f getTransform() {
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
	 * @return the translation
	 */
	public Vector2f getTranslation() {
		return m_transform.getTranslation();
	}

	/**
	 * Sets the translation.
	 * 
	 * @param translation
	 */
	public void setTranslation(Vector2f translation) {
		if (translation == null)
			throw new ComponentException("Cannot set a null translation!");
		setTranslation(translation.getX(), translation.getY());
	}

	/**
	 * Sets the translation.
	 * 
	 * @param x
	 * @param y
	 */
	public void setTranslation(float x, float y) {
		Transform2f newTrans = new Transform2f(m_transform);
		newTrans.setTranslation(new Vector2f(x, y));
		setTransform(newTrans);
	}

	/**
	 * Adds a rotation to the current rotation in radians with counter clockwise being positive.
	 * 
	 * @param rotate
	 */
	public void rotate(float rotate) {
		Transform2f newTrans = new Transform2f(m_transform);
		newTrans.setRotation(m_transform.getRotation() + rotate);
		setTransform(newTrans);
	}

	/**
	 * @return the rotation
	 */
	public float getRotation() {
		return m_transform.getRotation();
	}

	/**
	 * Sets the rotation in radians with counter clockwise being positive.
	 * 
	 * @param rotation
	 */
	public void setRotation(float rotation) {
		Transform2f newTrans = new Transform2f(m_transform);
		newTrans.setRotation(rotation);
		setTransform(newTrans);
	}

	/**
	 * @return the scale
	 */
	public Vector2f getScale() {
		return m_transform.getScale();
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

	/**
	 * Sets the scale.
	 * 
	 * @param scale
	 */
	public void setScale(Vector2f scale) {
		if (scale == null)
			throw new ComponentException("Cannot set a null translation!");
		setScale(scale.getX(), scale.getY());
	}

	/**
	 * Sets the scale.
	 * 
	 * @param x
	 * @param y
	 */
	public void setScale(float x, float y) {
		Transform2f newTrans = new Transform2f(m_transform);
		newTrans.setScale(new Vector2f(x, y));
		setTransform(newTrans);
	}

	/**
	 * @return whether the child Entities are translated relative to this Entity's translation
	 */
	public boolean isTranslateChildren() {
		return m_translateChildren;
	}

	/**
	 * Sets whether the child Entities are translated relative to this Entity's translation.
	 * 
	 * @param translateChildren
	 */
	public void setTranslateChildren(boolean translateChildren) {
		m_translateChildren = translateChildren;
	}

	/**
	 * @return whether the child Entities are rotated relative to this Entity's rotation.
	 */
	public boolean isRotateChildren() {
		return m_rotateChildren;
	}

	/**
	 * Sets whether the child Entities are rotated relative to this Entity's rotation.
	 * 
	 * @param rotateChildren
	 */
	public void setRotateChildren(boolean rotateChildren) {
		m_rotateChildren = rotateChildren;
	}

	/**
	 * @return whether the child Entities are scaled relative to this Entity's scale
	 */
	public boolean isScaleChildren() {
		return m_scaleChildren;
	}

	/**
	 * Sets whether the child Entities are scaled relative to this Entity's scale.
	 * 
	 * @param scaleChildren
	 */
	public void setScaleChildren(boolean scaleChildren) {
		m_scaleChildren = scaleChildren;
	}

	@Override
	public String getName() {
		return NAME;
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
