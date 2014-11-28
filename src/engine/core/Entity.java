package engine.core;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import commons.Transform2f;

/**
 * An Entity in a Scene. Child Entities all transform relative to each other.
 */
public class Entity implements EntityContainer {
	// the parent Entity / scene
	private EntityContainer m_parent;
	// the child Entities
	private Set<Entity> m_children;

	// the Scene this Entity belongs to
	private Scene m_scene;

	// the Entity's transformation in a 2d space.
	private Transform2f m_transform;
	// listeners for transformation / child changes
	private List<EntityListener> m_listeners;

	protected Entity(Scene scene, EntityContainer parent) {
		m_scene = scene;
		m_children = new LinkedHashSet<Entity>();
		m_listeners = new ArrayList<EntityListener>();
		m_transform = new Transform2f();
		m_parent = parent;
	}

	/**
	 * Destroys the instance fields of the Entity to make garbage collection easier.
	 */
	protected void destroy() {
		m_parent = null;
		m_scene = null;
		m_transform = null;
		m_children.clear();
		m_listeners.clear();
	}

	/**
	 * @return the Scene this Entity belongs to
	 */
	public Scene getScene() {
		return m_scene;
	}

	/**
	 * @return the parent EntityContainer
	 */
	public EntityContainer getParent() {
		return m_parent;
	}

	/**
	 * Sets the parent EntityContainer.
	 * 
	 * @param parent
	 */
	protected void setParent(EntityContainer parent) {
		if (parent == null)
			throw new SceneException("Cannot set parent to null!");

		for (EntityListener listener : m_listeners)
			listener.parentChanged(this, m_parent, parent);
		m_parent = parent;
	}

	/**
	 * Adds a child to the Entity.
	 * 
	 * @param child
	 */
	@Override
	public void addChild(Entity child) {
		if (m_children.contains(child))
			throw new EntityException("Entity already has this child!");

		m_children.add(child);
		for (EntityListener listener : m_listeners)
			listener.childAdded(this, child);
	}

	/**
	 * Removes a child from the Entity.
	 * 
	 * @param child
	 */
	@Override
	public void removeChild(Entity child) {
		if (!m_children.contains(child))
			throw new EntityException("Trying to remove a nonexistant child!");
		m_children.remove(child);

		for (EntityListener listener : m_listeners)
			listener.childRemoved(this, child);
	}

	/**
	 * @return the children of this Entity
	 */
	public Set<Entity> getChildren() {
		return m_children;
	}

	/**
	 * Do not modify this transform as it will not notify the listeners, and the physics system will simply overwrite
	 * your new values. Call setTransform() instead.
	 * 
	 * @return the Entity's transform.
	 */
	public Transform2f getTransform() {
		return m_transform;
	}

	/**
	 * Sets the Entity's transform and notifies its listeners.
	 * 
	 * @param transform
	 */
	public void setTransform(Transform2f transform) {
		if (transform == null)
			throw new EntityException("Cannot set a null transform!");
		for (EntityListener listener : m_listeners)
			listener.transformSet(this, m_transform, transform);
		m_transform = transform;
	}

	public String toTabbedString(int tabs) {
		String tabString = makeTabs(tabs);
		String string = "";
		string += tabString + "Entity " + super.toString() + ": \n";
		for (Entity e : m_children) {
			string += /*tabString + "Child: \n" + */e.toTabbedString(tabs + 1);
		}
		return string;
	}

	private String makeTabs(int tabs) {
		String string = "";
		for (int i = 0; i < tabs; i++) {
			string += "\t";
		}
		return string;
	}

	@Override
	public String toString() {
		String string = "";
		string += "\n----------------------------------------------\n";
		string += "Entity " + super.toString() + ": \n";
		for (Entity e : m_children) {
			string += /*"Child: \n" + */e.toTabbedString(1);
		}
		string += "\n----------------------------------------------";
		return string;
	}
}
