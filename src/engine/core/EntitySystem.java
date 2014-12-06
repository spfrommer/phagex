package engine.core;

/**
 * Acts as a centralized data manager. Allows for the modification of many Entities each update.
 */
public interface EntitySystem {
	/**
	 * Called when an Entity that matches the EntitySystem's EntityFilter is added to the Scene.
	 * 
	 * @param entity
	 * @param parent
	 * @param scene
	 */
	public void entityAdded(Entity entity, TreeNode parent, Scene scene);

	/**
	 * Called when an Entity that matches the EntitySystem's EntityFilter is removed from the Scene.
	 * 
	 * @param entity
	 * @param parent
	 * @param scene
	 */
	public void entityRemoved(Entity entity, TreeNode parent, Scene scene);

	/**
	 * Called when an Entity is moved to a new parent.
	 * 
	 * @param entity
	 * @param oldParent
	 * @param newParent
	 * @param scene
	 */
	public void entityMoved(Entity entity, TreeNode oldParent, TreeNode newParent, Scene scene);

	/**
	 * Called to update any centralized process - such as a physics World, for example.
	 * 
	 * @param time
	 */
	public void update(float time);

	/**
	 * Called to update an Entity which matches the filter.
	 * 
	 * @param entity
	 */
	public void updateEntity(Entity entity);

	/**
	 * Called after all the systems are finished, but before the Scripts are updated.
	 */
	public void postUpdate();

	/**
	 * @return the EntityFilter that's used when system.updateEntity(entity) is called
	 */
	public EntityFilter getUpdateFilter();

	/**
	 * @return the EntityFilter that's used when system.entityAdded/Removed/Moved is called
	 */
	public EntityFilter getEntityEventFilter();
}
