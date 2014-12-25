package engine.core;

import java.util.Arrays;
import java.util.List;

import engine.core.exceptions.EntityException;

/**
 * Returns true if the Entity has all of the required Components, fields, and tags.
 */
public class SimpleEntityFilter implements EntityFilter {
	private List<String> m_requiredComponents;
	private TagList m_requiredTags;
	private boolean m_topLevelOnly;

	public SimpleEntityFilter(String[] requiredComponents, String[] requiredTags, boolean topLevelOnly) {
		m_requiredComponents = Arrays.asList(requiredComponents);
		m_requiredTags = new TagList(requiredTags);
		m_topLevelOnly = topLevelOnly;
	}

	@Override
	public boolean matches(Entity entity) {
		if (entity == null)
			throw new EntityException("Cannot apply filter to null Entity!");

		if (m_topLevelOnly) {
			if (!(entity.tree().getParent() instanceof Scene))
				return false;
		}

		TagList tags = entity.getCTags().getTags();
		List<String> components = entity.components().allNames();
		if (tags.containsAll(m_requiredTags) && components.containsAll(m_requiredComponents)) {
			return true;
		}
		return false;
	}
}
