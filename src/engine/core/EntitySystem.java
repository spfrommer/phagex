package engine.core;

import engine.core.script.XScript;

/**
 * Acts as a centralized data manager. Allows for the modification of many Entities each update.
 */
public interface EntitySystem {
	/**
	 * Called when the Scene changes.
	 * 
	 * @param oldScene
	 * @param newScene
	 */
	public void sceneChanged(Scene oldScene, Scene newScene);

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
	 * Called when a script is added. Allows the EntitySystem to inject ScriptObjects.
	 * 
	 * @param entity
	 * @param script
	 * @param scene
	 */
	public void scriptAdded(Entity entity, XScript script, Scene scene);

	/**
	 * Called to update any centralized process - such as a physics World, for example.
	 * @param scene
	 * @param time
	 */
	public void update(Scene scene, float time);

	/**
	 * Called to update an Entity which matches the filter.
	 * 
	 * @param entity
	 * @param scene
	 * @param time TODO
	 */
	public void updateEntity(Entity entity, Scene scene, float time);

	/**
	 * Called after all the systems are finished, but before the Scripts are updated.
	 * 
	 * @param scene
	 */
	public void postUpdate(Scene scene);

	/**
	 * @return the EntityFilter that's used when system.updateEntity(entity) is called
	 */
	public EntityFilter getUpdateFilter();

	/**
	 * @return the EntityFilter that's used when system.entityAdded/Removed/Moved is called
	 */
	public EntityFilter getEntityEventFilter();
}
