package engine.core;

/**
 * Returns true if any of the subfilters return true.
 */
public class CompoundEntityFilter implements EntityFilter {
	private EntityFilter[] m_filters;

	public CompoundEntityFilter(EntityFilter[] filters) {
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
