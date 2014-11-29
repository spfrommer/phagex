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
	 * Returns the identifiers of this Component's data.
	 * 
	 * @return
	 */
	public String[] getIdentifiers();

	/**
	 * Returns the data corresponding to the identifier.
	 * 
	 * @param identifier
	 * @return
	 */
	public Object getData(String identifier);

	/**
	 * Sets the data corresponding to the identifier.
	 * 
	 * @param identifier
	 * @param data
	 */
	public void setData(String identifier, Object data);

	/**
	 * Returns a builder which will make an exact copy of this Component.
	 * 
	 * @return
	 */
	public ComponentBuilder<? extends Component> getBuilder();
}
