package engine.core;

import engine.core.exceptions.ComponentException;
import engine.core.exceptions.TagException;

/**
 * A Component which holds a TagList.
 */
public class CTags implements Component {
	public static final String NAME = "tags";

	private TagList m_tags;

	public CTags(TagList tags) {
		m_tags = tags;
	}

	/**
	 * @return the TagList
	 */
	public TagList getTags() {
		return m_tags;
	}

	/**
	 * Sets the tags.
	 * 
	 * @param tags
	 */
	public void setTags(TagList tags) {
		if (tags == null)
			throw new ComponentException("Cannot set a null TagList!");
		m_tags = tags;
	}

	/**
	 * Adds a tag.
	 * 
	 * @param tag
	 */
	public void addTag(String tag) {
		if (tag == null)
			throw new TagException("Cannot add a null tag!");
		if (m_tags.hasTag(tag))
			throw new TagException("Cannot add same tag twice!");
		m_tags = m_tags.newAdd(tag);
	}

	/**
	 * Removes a tag.
	 * 
	 * @param tag
	 */
	public void removeTag(String tag) {
		if (tag == null)
			throw new TagException("Cannot remove a null tag!");
		m_tags = m_tags.newRemove(tag);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public ComponentBuilder<CTags> getBuilder() {
		ComponentBuilder<CTags> builder = new ComponentBuilder<CTags>() {
			@Override
			public CTags build() {
				return new CTags(new TagList(m_tags));
			}

			@Override
			public String getName() {
				return NAME;
			}

		};
		return builder;
	}

	@Override
	public String toString() {
		return m_tags.toString();
	}
}
