package engine.core;

/**
 * Returns true if any of the subfilters return true.
 */
public class CompositeEntityFilter implements EntityFilter {
	private EntityFilter[] m_filters;

	public CompositeEntityFilter(EntityFilter[] filters) {
		m_filters = filters;
	}

	@Override
	public boolean matches(Entity entity) {
		for (EntityFilter filter : m_filters)
			if (filter.matches(entity))
				return true;
		return false;
	}
}
