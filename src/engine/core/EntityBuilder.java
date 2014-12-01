package engine.core;

import java.util.ArrayList;
import java.util.List;

import engine.core.exceptions.ComponentException;

/**
 * Builds an Entity from a bunch of ComponentBuilders. CTransform and CScriptData builders should not be added, as they
 * are already added automatically. Scripts are added later. To build the Entity, call scene.createEntity(parent,
 * builder).
 */
public class EntityBuilder {
	private List<ComponentBuilder<? extends Component>> m_builders;

	/**
	 * Initializes an EntityBuilder.
	 */
	public EntityBuilder() {
		m_builders = new ArrayList<ComponentBuilder<? extends Component>>();
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
	 * Returns the individual ComponentBuilders.
	 * 
	 * @return
	 */
	protected List<ComponentBuilder<? extends Component>> getComponentBuilders() {
		return m_builders;
	}
}
