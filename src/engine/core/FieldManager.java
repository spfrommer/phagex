package engine.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.core.exceptions.ComponentException;
import engine.core.exceptions.IdentifierException;

/**
 * Manages the Fields for an Entity.
 */
public class FieldManager {
	// says which Component has the data for an identifier
	private Map<String, Component> m_dataLocations;
	// reverse of dataLocations
	private Map<Component, List<String>> m_dataReversed;
	// the CSCriptData of the Entity
	private CScriptData m_scriptData;

	/**
	 * A FieldManager has to be initialized after the ComponentManager has been added.
	 * 
	 * @param entity
	 */
	public FieldManager(Entity entity) {
		m_dataLocations = new HashMap<String, Component>();
		m_dataReversed = new HashMap<Component, List<String>>();

		m_scriptData = entity.getCScriptData();

		ComponentManager components = entity.components();
		List<Component> allComponents = components.all();

		for (Component c : allComponents) {
			m_dataReversed.put(c, new ArrayList<String>());
			for (String identifier : c.getIdentifiers()) {
				if (m_dataLocations.containsKey(identifier))
					throw new ComponentException("Identifier duplicate: " + identifier);
				m_dataLocations.put(identifier, c);
				m_dataReversed.get(c).add(identifier);
			}
		}
	}

	/**
	 * @param identifier
	 * @return whether the Field exists
	 */
	public boolean has(String identifier) {
		if (identifier == null)
			throw new IdentifierException("Cannot check identifier with a null String!");

		return m_dataLocations.containsKey(identifier);
	}

	/**
	 * @param identifier
	 * @return the data in the field
	 */
	public Object get(String identifier) {
		if (identifier == null)
			throw new IdentifierException("Cannot get a field with a null String!");
		if (!m_dataLocations.containsKey(identifier))
			throw new IdentifierException("No field exists for the identifier: " + identifier);

		return m_dataLocations.get(identifier).getData(identifier);
	}

	/**
	 * Sets a data field. If the field does not exist in the components, it is created in the CScriptData component.
	 * 
	 * @param identifier
	 * @param data
	 */
	public void set(String identifier, Object data) {
		if (identifier == null)
			throw new IdentifierException("Cannot set a field with a null String!");
		if (data == null)
			throw new IdentifierException("Cannot set null data for identifier: " + identifier);

		if (m_dataLocations.containsKey(identifier)) {
			m_dataLocations.get(identifier).setData(identifier, data);
		} else {
			m_scriptData.setData(identifier, data);
		}
	}

	/**
	 * Tells the Manager to reload the data from the Component.
	 * 
	 * @param component
	 */
	protected void reloadFields(Component component) {
		if (component == null)
			throw new ComponentException("Cannot reload data for null Component!");

		List<String> newIdents = Arrays.asList(component.getIdentifiers());
		for (String ident : newIdents) {
			if (!m_dataLocations.containsKey(ident)) {
				m_dataLocations.put(ident, component);
				m_dataReversed.get(component).add(ident);
			}
		}

		List<String> oldIdents = m_dataReversed.get(component);
		for (String ident : oldIdents) {
			if (!newIdents.contains(ident)) {
				m_dataLocations.remove(ident);
				m_dataReversed.get(component).remove(ident);
			}
		}
	}

	/**
	 * @return a List of the Entity's fields
	 */
	public List<String> all() {
		return new ArrayList<String>(m_dataLocations.keySet());
	}
}
