package engine.core;

import java.util.ArrayList;
import java.util.List;

import engine.core.exceptions.ComponentException;

/**
 * Builds an Entity from a bunch of ComponentBuilders. CTransform builders should not be added, as they are already
 * added automatically.
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
	 * Adds a ComponentBuilder to the EntityBuilder.
	 * 
	 * @param builder
	 */
	public void addComponentBuilder(ComponentBuilder<? extends Component> builder) {
		if (builder == null)
			throw new ComponentException("Trying to add a null builder.");
		if (builder.getName() == null)
			throw new ComponentException("Trying to add a builder with a null name.");

		if (builder.getName().equals(CTransform.NAME))
			throw new ComponentException(
					"Trying to add a CTransform component to an EntityBuilder - this is not required as the CTransform is added by default.");
		for (ComponentBuilder<? extends Component> b : m_builders) {
			if (!b.getName().equals(builder.getName())) {
				m_builders.add(builder);
			} else {
				throw new ComponentException("Trying to build a Entity with two of the same type of Components.");
			}
		}
	}

	/**
	 * Removes a ComponentBuilder from the EntityBuilder.
	 * 
	 * @param builder
	 */
	public void removeComponentBuilder(ComponentBuilder<? extends Component> builder) {
		if (builder == null)
			throw new ComponentException("Trying to remove a null builder.");
		if (builder.getName() == null)
			throw new ComponentException("Trying to remove a builder with a null name.");

		for (ComponentBuilder<? extends Component> b : m_builders) {
			if (b.getName().equals(builder.getName())) {
				m_builders.remove(builder);
			}
		}
	}
}
