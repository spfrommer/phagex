package engine.core;

public interface EntityContainer {
	/**
	 * Adds an Entity to the container.
	 * 
	 * @param entity
	 */
	public void addChild(Entity entity);

	/**
	 * Removes an Entity from the container.
	 * 
	 * @param entity
	 */
	public void removeChild(Entity entity);
}
