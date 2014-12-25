package engine.core;

/**
 * A Component of an Entity. Contains only data.
 */
public interface Component {
	/**
	 * Gets the name of the component.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Returns a builder which will make an exact copy of this Component.
	 * 
	 * @return
	 */
	public ComponentBuilder<? extends Component> getBuilder();
}
