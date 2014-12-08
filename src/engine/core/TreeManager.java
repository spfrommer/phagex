package engine.core;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import engine.core.exceptions.EntityException;

/**
 * Manages the relationship tree of an Entity.
 */
public class TreeManager {
	private Entity m_entity;

	// the parent Entity / scene
	private TreeNode m_parent;
	// the child Entities
	private Set<Entity> m_children;
	// the map of child names to child Entities
	private Map<String, Entity> m_childNames;

	public TreeManager(Entity entity, TreeNode parent) {
		m_entity = entity;
		m_parent = parent;
		m_children = new LinkedHashSet<Entity>();
		m_childNames = new HashMap<String, Entity>();
	}

	/**
	 * Clears the references to make garbage collection simpler.
	 */
	protected void destroy() {
		m_parent = null;
		m_children.clear();
	}

	/**
	 * @return the parent TreeNode
	 */
	public TreeNode getParent() {
		return m_parent;
	}

	/**
	 * Sets the parent TreeNode and the new relative transform.
	 * 
	 * @param parent
	 */
	protected void setParent(TreeNode parent/*, Transform2f transform*/) {
		if (parent == null)
			throw new EntityException("Cannot set parent to null!");
		// if (transform == null)
		// throw new EntityException("Transform cannot be null!");

		// Transform2f oldTransform = m_entity.getCTransform().getTransform();

		m_parent = parent;
		// m_entity.getCTransform().quietSetTransform(transform);

		// for (EntityListener listener : m_entity.getListeners())
		// listener.parentChanged(m_entity, m_parent, parent, oldTransform, transform, m_entity.getScene());
		// this no longer calls the listeners - it is done in Scene.moveEntity
	}

	/**
	 * Adds a child to the Entity.
	 * 
	 * @param child
	 */
	public void addChild(Entity child) {
		if (m_children.contains(child))
			throw new EntityException("Entity already has this child!");
		if (m_childNames.keySet().contains(child.getName()))
			throw new EntityException("No two children with the same name allowed!");

		m_children.add(child);
		m_childNames.put(child.getName(), child);

		for (EntityListener listener : m_entity.getListeners())
			listener.childAdded(m_entity, child, m_entity.getScene());
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
			listener.childRemoved(m_entity, child, m_entity.getScene());
	}

	/**
	 * @param name
	 * @return whether the child exists
	 */
	public boolean hasChild(String name) {
		if (name == null)
			throw new EntityException("Cannot check child for null name!");

		return m_childNames.containsKey(name);
	}

	/**
	 * @return the children of this Entity
	 */
	public Set<Entity> getChildren() {
		return m_children;
	}

	/**
	 * Returns the child for the name.
	 * 
	 * @param name
	 * @return
	 */
	public Entity getChild(String name) {
		if (!m_childNames.containsKey(name))
			throw new EntityException("No child for name: " + name);
		return m_childNames.get(name);
	}

	/**
	 * Called when a child's name changes.
	 * 
	 * @param child
	 * @param oldName
	 * @param newName
	 */
	protected void childNameChanged(TreeNode child, String oldName, String newName) {
		if (!m_children.contains(child))
			throw new EntityException("This TreeNode is not my child!");
		if (m_childNames.keySet().contains(newName))
			throw new EntityException("No two children with the same name allowed!");

		Entity entityChild = (Entity) child;
		m_childNames.remove(oldName);
		m_childNames.put(newName, entityChild);
	}
}
