package engine.core;

/**
 * Filters Entities.
 */
public interface EntityFilter {
	/**
	 * @param entity
	 * @return whether the filter matches.
	 */
	public boolean matches(Entity entity);
}
