package engine.core;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Creates, destroys, and moves Entities in the tree.
 */
public class Scene implements EntityContainer {
	// the top level entities are practically the children of the EntityContainer aspect of the Scene
	private Set<Entity> m_topLevelEntities;
	private Set<Entity> m_allEntities;

	public Scene() {
		m_topLevelEntities = new LinkedHashSet<Entity>();
		m_allEntities = new LinkedHashSet<Entity>();
	}

	/**
	 * Creates a child Entity of a parent.
	 * 
	 * @param parent
	 * @return
	 */
	public Entity createEntity(EntityContainer parent) {
		if (parent == null)
			throw new SceneException("Cannot create an Entity with a null parent!");
		if (!m_allEntities.contains(parent) && !(this == parent))
			throw new SceneException("Trying to create an Entity in a container not in the Scene!");
		Entity entity = new Entity(this, parent);
		parent.addChild(entity);
		m_allEntities.add(entity);
		return entity;
	}

	/**
	 * Removes an Entity from the Scene.
	 * 
	 * @param entity
	 */
	public void destroyEntity(Entity entity) {
		if (!m_allEntities.contains(entity))
			throw new SceneException("Trying to destroy an Entity not in the Scene!");

		entity.getParent().removeChild(entity);
		recursiveRemove(entity);
	}

	/**
	 * Recursively removes the Entity and its children from the allEntities Set. Will also call destroy() on the
	 * Entities.
	 * 
	 * @param entity
	 */
	private void recursiveRemove(Entity entity) {
		for (Entity child : entity.getChildren()) {
			recursiveRemove(child);
		}
		m_allEntities.remove(entity);
		entity.destroy();
	}

	/**
	 * Moves an Entity to a new parent.
	 * 
	 * @param entity
	 * @param newParent
	 */
	public void moveEntity(Entity entity, EntityContainer newParent) {
		if (entity == null)
			throw new SceneException("Tried to move a null parent!");
		if (newParent == null)
			throw new SceneException("Tried to move an Entity into a null parent!");
		if (!m_allEntities.contains(newParent) && !(this == newParent))
			throw new SceneException("Trying to move an Entity to a container not in the Scene!");
		entity.getParent().removeChild(entity);
		newParent.addChild(entity);
		entity.setParent(newParent);
	}

	/**
	 * Adds a TOP-LEVEL child to the Scene. This should not be called. Use createEntity() instead.
	 */
	@Override
	public void addChild(Entity entity) {
		if (m_topLevelEntities.contains(entity))
			throw new SceneException("Entity is already in Scene!");
		m_topLevelEntities.add(entity);
	}

	/**
	 * Removes a TOP-LEVEL child from the Scene. Really, you should call destroyEntity(Entity) if you want to completely
	 * destroy it.
	 */
	@Override
	public void removeChild(Entity entity) {
		if (!m_topLevelEntities.contains(entity))
			throw new SceneException("Trying to remove an Entity not in the top level!");
		m_topLevelEntities.remove(entity);
	}
}
