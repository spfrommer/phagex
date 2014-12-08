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
	 */
	public void childAdded(Entity entity, Entity child, Scene scene);

	/**
	 * Called when a child is removed from an Entity.
	 * 
	 * @param entity
	 * @param child
	 * @param scene
	 */
	public void childRemoved(Entity entity, Entity child, Scene scene);

	/**
	 * Called when the parent of an Entity changes.
	 * 
	 * @param entity
	 * @param oldParent
	 * @param newParent
	 * @param oldLocalTransform
	 * @param newLocalTransform
	 * @param oldWorldTransform
	 * @param newWorldTransform
	 * @param scene
	 */
	public void parentChanged(Entity entity, TreeNode oldParent, TreeNode newParent, Transform2f oldLocalTransform,
			Transform2f newLocalTransform, Transform2f oldWorldTransform, Transform2f newWorldTransform, Scene scene);

	/**
	 * Called when the transform is set on an Entity.
	 * 
	 * @param entity
	 * @param oldLocalTransform
	 * @param newLocalTransform
	 * @param scene
	 */
	public void transformSet(Entity entity, Transform2f oldLocalTransform, Transform2f newLocalTransform, Scene scene);
}
