package engine.core;

/**
 * Acts as a centralized data manager. Allows for the modification of many Entities each update.
 */
public interface EntitySystem {
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
	 * @return the EntityFilter
	 */
	public EntityFilter getFilter();
}
