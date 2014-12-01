package engine.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.core.exceptions.ComponentException;

/**
 * Manages the components and data of an Entity.
 */
public class ComponentManager {
	// maps the name of the Component to the Component
	private Map<String, Component> m_components;

	/**
	 * Initializes the ComponentManager.
	 * 
	 * @param entity
	 * @param components
	 * @param tags
	 * @param transform
	 * @param script
	 */
	public ComponentManager(Entity entity, List<Component> components) {
		m_components = new HashMap<String, Component>();

		m_components.put(entity.getCTags().getName(), entity.getCTags());
		m_components.put(entity.getCTransform().getName(), entity.getCTransform());
		m_components.put(entity.getCScriptData().getName(), entity.getCScriptData());
		for (Component comp : components) {
			String name = comp.getName();
			if (m_components.containsKey(name))
				throw new ComponentException("Name duplicate: " + name);

			m_components.put(name, comp);
		}
	}

	/**
	 * @param name
	 * @return the Component for the specified name
	 */
	public Component get(String name) {
		if (name == null)
			throw new ComponentException("Cannot get a Component with a null String!");
		if (!m_components.containsKey(name))
			throw new ComponentException("No Component exists for the name: " + name);

		return m_components.get(name);
	}

	/**
	 * @param name
	 * @return whether the Component exists
	 */
	public boolean has(String name) {
		if (name == null)
			throw new ComponentException("Cannot check Component with a null String!");
		return m_components.containsKey(name);
	}

	/**
	 * @return a List of Components
	 */
	public List<Component> all() {
		return new ArrayList<Component>(m_components.values());
	}

	/**
	 * @return a List of the Component names
	 */
	public List<String> allNames() {
		return new ArrayList<String>(m_components.keySet());
	}
}
