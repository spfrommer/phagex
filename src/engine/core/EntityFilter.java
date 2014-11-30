package engine.core;

import java.util.Arrays;
import java.util.List;

import engine.core.exceptions.EntityException;

/**
 * A Filter for the System.
 */
public class EntityFilter {
	private List<String> m_requiredComponents;
	private List<String> m_requiredFields;
	private TagList m_requiredTags;

	public EntityFilter(String[] requiredComponents, String[] requiredFields, String[] requiredTags) {
		m_requiredComponents = Arrays.asList(requiredComponents);
		m_requiredFields = Arrays.asList(requiredFields);
		m_requiredTags = new TagList(requiredTags);
	}

	/**
	 * Check if the filter matches the Entity. This is true if the Entity has all of the required Components, fields,
	 * and tags.
	 * 
	 * @param entity
	 * @return
	 */
	public boolean matches(Entity entity) {
		if (entity == null)
			throw new EntityException("Cannot apply filter to null Entity!");
		TagList tags = entity.getCTags().getTags();
		List<String> components = entity.components().allComponentNames();
		List<String> fields = entity.components().allFields();
		if (tags.containsAll(m_requiredTags) && components.containsAll(m_requiredComponents)
				&& fields.containsAll(m_requiredFields)) {
			return true;
		}
		return false;
	}
}
