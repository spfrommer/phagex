package engine.core;

import commons.Transform2f;

/**
 * Listens for transform changes on an Entity.
 */
public interface EntityListener {
	/**
	 * Called when a child is added to an Entity.
	 * 
	 * @param entity
	 * @param child
	 * @param scene
	 *            TODO
	 */
	public void childAdded(Entity entity, Entity child, Scene scene);

	/**
	 * Called when a child is removed from an Entity.
	 * 
	 * @param entity
	 * @param child
	 * @param scene
	 *            TODO
	 */
	public void childRemoved(Entity entity, Entity child, Scene scene);

	/**
	 * Called when the parent of an Entity changes.
	 * 
	 * @param entity
	 * @param oldParent
	 * @param newParent
	 * @param oldTransform
	 * @param newTransform
	 * @param scene
	 *            TODO
	 */
	public void parentChanged(Entity entity, TreeNode oldParent, TreeNode newParent, Transform2f oldTransform,
			Transform2f newTransform, Scene scene);

	public void transformSet(Entity entity, Transform2f oldTransform, Transform2f newTransform, Scene scene);
}
