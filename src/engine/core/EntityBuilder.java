package engine.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import commons.Transform2f;

import engine.core.exceptions.ComponentException;
import engine.core.exceptions.EntityException;
import engine.core.exceptions.XScriptException;
import engine.core.script.XScript;

/**
 * Builds an Entity from a bunch of ComponentBuilders. CTransform and CScriptData builders should not be added, as they
 * are already added automatically. Scripts are added later. To build the Entity, call scene.createEntity(parent,
 * builder).
 */
public class EntityBuilder {
	private List<ComponentBuilder<? extends Component>> m_builders;
	protected List<XScript> m_scripts;
	private TagList m_tags = new TagList();
	private Transform2f m_transform = new Transform2f();
	private Map<String, EntityBuilder> m_childBuilders;

	/**
	 * Initializes an EntityBuilder.
	 */
	public EntityBuilder() {
		m_builders = new ArrayList<ComponentBuilder<? extends Component>>();
		m_childBuilders = new HashMap<String, EntityBuilder>();
		m_scripts = new ArrayList<XScript>();
	}

	/**
	 * @return a deep clone of the TagList
	 */
	public TagList getTagList() {
		return new TagList(m_tags);
	}

	/**
	 * Sets the TagList.
	 * 
	 * @param tags
	 */
	public void setTagList(TagList tags) {
		m_tags = tags;
	}

	/**
	 * @return the Transform2f
	 */
	public Transform2f getTransform() {
		return m_transform;
	}

	/**
	 * Sets the transform.
	 * 
	 * @param transform
	 */
	public void setTransform(Transform2f transform) {
		m_transform = transform;
	}

	/**
	 * Adds a child builder.
	 * 
	 * @param name
	 * @param builder
	 */
	public void addChildBuilder(String name, EntityBuilder builder) {
		if (builder == null)
			throw new EntityException("Cannot add null child builder!");

		m_childBuilders.put(name, builder);
	}

	/**
	 * Removes a child builder.
	 * 
	 * @param name
	 * @param builder
	 */
	public void removeChildBuilder(String name, EntityBuilder builder) {
		if (builder == null)
			throw new EntityException("Cannot remove a null child builder!");
		if (!m_childBuilders.containsKey(name))
			throw new EntityException("Cannot remove a nonexistant builder!");

		m_childBuilders.remove(name);
	}

	/**
	 * Adds the component builder returned by component.getBuilder();
	 * 
	 * @param component
	 */
	public void addComponentBuilder(Component component) {
		if (component == null)
			throw new ComponentException("Cannot make builder from null Component!");

		m_builders.add(component.getBuilder());
	}

	/**
	 * Adds a ComponentBuilder to the EntityBuilder.
	 * 
	 * @param builder
	 */
	public void addComponentBuilder(ComponentBuilder<? extends Component> builder) {
		if (builder == null)
			throw new ComponentException("Trying to add a null builder!");
		if (builder.getName() == null)
			throw new ComponentException("Trying to add a builder with a null name!");

		if (builder.getName().equals(CTransform.NAME) || builder.getName().equals(CTags.NAME)
				|| builder.getName().equals(CScriptData.NAME))
			throw new ComponentException("Trying to add a " + builder.getName()
					+ " component to an EntityBuilder - this is not required as it is added by default!");

		for (ComponentBuilder<? extends Component> b : m_builders) {
			if (b.getName().equals(builder.getName())) {
				throw new ComponentException("Trying to build a Entity with two of the same type of Components!");
			}
		}
		m_builders.add(builder);
	}

	/**
	 * Removes a ComponentBuilder from the EntityBuilder.
	 * 
	 * @param builder
	 */
	public void removeComponentBuilder(ComponentBuilder<? extends Component> builder) {
		if (builder == null)
			throw new ComponentException("Trying to remove a null builder!");
		if (builder.getName() == null)
			throw new ComponentException("Trying to remove a builder with a null name!");
		if (!m_builders.contains(builder))
			throw new ComponentException("Trying to remove a builder which wasn't added!");

		m_builders.remove(builder);
	}

	/**
	 * Adds a XScript to the EntityBuilder.
	 * 
	 * @param script
	 */
	public void addScript(XScript script) {
		if (script == null)
			throw new XScriptException("Adding null XScript!");

		m_scripts.add(script);
	}

	/**
	 * Removes a XScript from the EntityBuilder.
	 * 
	 * @param script
	 */
	public void removeScript(XScript script) {
		if (script == null)
			throw new XScriptException("Removing null XScript!");
		if (!m_scripts.contains(script))
			throw new XScriptException("Removing nonexistant XScript!");

		m_scripts.remove(script);
	}

	protected List<XScript> getScripts() {
		return m_scripts;
	}

	/**
	 * Returns the script data. This can be modified.
	 * 
	 * @return the script data
	 */
	public Map<String, Object> getScriptData() {
		return m_scriptData;
	}

	/**
	 * Sets the script data.
	 * 
	 * @param scriptData
	 */
	public void setScriptData(Map<String, Object> scriptData) {
		m_scriptData = scriptData;
	}

	/**
	 * Returns the individual ComponentBuilders.
	 * 
	 * @return
	 */
	public List<ComponentBuilder<? extends Component>> getComponentBuilders() {
		return m_builders;
	}

	public List<XScript> getScripts() {
		return m_scripts;
	}

	/**
	 * @return the EntityBuilders for the children
	 */
	public Map<String, EntityBuilder> getEntityBuilders() {
		return m_childBuilders;
	}
}
