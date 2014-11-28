package engine.core;

import commons.Transform2f;

/**
 * Listens for transform changes on an Entity.
 */
public interface EntityListener {
	/**
	 * Called when setTransform() is called on an Entity.
	 * 
	 * @param entity
	 * @param oldTransform
	 * @param newTransform
	 */
	public void transformSet(Entity entity, Transform2f oldTransform, Transform2f newTransform);

	/**
	 * Called when a child is added to an Entity.
	 * 
	 * @param entity
	 * @param child
	 */
	public void childAdded(Entity entity, Entity child);

	/**
	 * Called when a child is removed from an Entity.
	 * 
	 * @param entity
	 * @param child
	 */
	public void childRemoved(Entity entity, Entity child);

	/**
	 * Called when the parent of an Entity changes.
	 * 
	 * @param entity
	 * @param oldParent
	 * @param newParent
	 */
	public void parentChanged(Entity entity, EntityContainer oldParent, EntityContainer newParent);
}
