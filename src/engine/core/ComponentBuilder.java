package engine.core;

/**
 * Builds a Component.
 * 
 * @param <T>
 *            the type of Component
 */
public interface ComponentBuilder<T extends Component> {
	/**
	 * Builds the Component.
	 * 
	 * @return
	 */
	public T build();

	/**
	 * Returns the name of the Component.
	 * 
	 * @return
	 */
	public String getName();
}
