package engine.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.core.exceptions.ComponentException;
import engine.core.exceptions.IdentifierException;

/**
 * Manages the components and data of an Entity.
 */
public class ComponentManager {
	// maps the name of the Component to the Component
	private Map<String, Component> m_components;
	// says which Component has the data for an identifier
	private Map<String, Component> m_dataLocations;

	/**
	 * Initializes the ComponentManager.
	 * 
	 * @param entity
	 * @param components
	 * @param transform
	 */
	public ComponentManager(List<Component> components, CTransform transform) {
		m_components = new HashMap<String, Component>();
		m_dataLocations = new HashMap<String, Component>();

		m_components.put(transform.getName(), transform);
		for (Component comp : components) {
			String name = comp.getName();
			if (m_components.containsKey(name))
				throw new ComponentException("Name duplicate: " + name);

			m_components.put(name, comp);
		}

		for (Component c : m_components.values()) {
			for (String identifier : c.getIdentifiers()) {
				if (m_dataLocations.containsKey(identifier))
					throw new ComponentException("Identifier duplicate: " + identifier);
				m_dataLocations.put(identifier, c);
			}
		}
	}

	/**
	 * @param name
	 * @return the Component for the specified name
	 */
	public Component getComponent(String name) {
		if (name == null)
			throw new ComponentException("Cannot get a Component with a null String!");
		if (!m_components.containsKey(name))
			throw new ComponentException("No Component exists for the name: " + name);

		return m_components.get(name);
	}

	/**
	 * @param identifier
	 * @return the data in the field
	 */
	public Object getField(String identifier) {
		if (identifier == null)
			throw new IdentifierException("Cannot get a field with a null String!");
		if (!m_dataLocations.containsKey(identifier))
			throw new IdentifierException("No field exists for the identifier: " + identifier);

		return m_dataLocations.get(identifier).getData(identifier);
	}

	/**
	 * @param name
	 * @return whether the Component exists
	 */
	public boolean hasComponent(String name) {
		if (name == null)
			throw new ComponentException("Cannot check Component with a null String!");
		return m_components.containsKey(name);
	}

	/**
	 * @param identifier
	 * @return whether the Field exists
	 */
	public boolean hasField(String identifier) {
		if (identifier == null)
			throw new IdentifierException("Cannot check identifier with a null String!");

		return m_dataLocations.containsKey(identifier);
	}
}
