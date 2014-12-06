package engine;

import engine.core.Entity;

/**
 * A Filter for the System.
 */
public interface EntityFilter {
	/**
	 * Check if the filter matches the Entity.
	 * 
	 * @param entity
	 * @return
	 */
	public boolean matches(Entity entity);
}
