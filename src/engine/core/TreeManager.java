package engine.core;

import java.util.LinkedHashSet;
import java.util.Set;

import commons.Transform2f;
import engine.core.exceptions.EntityException;

/**
 * Manages the relationship tree of an Entity.
 */
public class TreeManager {
	private Entity m_entity;

	// the parent Entity / scene
	private EntityContainer m_parent;
	// the child Entities
	private Set<Entity> m_children;

	public TreeManager(Entity entity, EntityContainer parent) {
		m_entity = entity;
		m_parent = parent;
		m_children = new LinkedHashSet<Entity>();
	}

	protected void destroy() {
		m_parent = null;
		m_children.clear();
	}

	/**
	 * @return the parent EntityContainer
	 */
	public EntityContainer getParent() {
		return m_parent;
	}

	/**
	 * Sets the parent EntityContainer and the new relative transform.
	 * 
	 * @param parent
	 */
	protected void setParent(EntityContainer parent, Transform2f transform) {
		if (parent == null)
			throw new EntityException("Cannot set parent to null!");
		if (transform == null)
			throw new EntityException("Transform cannot be null!");

		for (EntityListener listener : m_entity.getListeners())
			listener.parentChanged(m_entity, m_parent, parent, m_entity.getTransform(), transform);

		m_parent = parent;
		m_entity.setTransform(transform);
	}

	/**
	 * Adds a child to the Entity.
	 * 
	 * @param child
	 */
	public void addChild(Entity child) {
		if (m_children.contains(child))
			throw new EntityException("Entity already has this child!");

		m_children.add(child);
		for (EntityListener listener : m_entity.getListeners())
			listener.childAdded(m_entity, child);
	}

	/**
	 * Removes a child from the Entity.
	 * 
	 * @param child
	 */
	public void removeChild(Entity child) {
		if (!m_children.contains(child))
			throw new EntityException("Trying to remove a nonexistant child!");
		m_children.remove(child);

		for (EntityListener listener : m_entity.getListeners())
			listener.childRemoved(m_entity, child);
	}

	/**
	 * @return the children of this Entity
	 */
	public Set<Entity> getChildren() {
		return m_children;
	}
}
