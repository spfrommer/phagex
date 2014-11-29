package engine.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import engine.core.exceptions.TagException;

/**
 * An immutable List of tags. Taken from big-phage engine.
 */
public class TagList implements Iterable<String> {
	private ArrayList<String> m_tags = new ArrayList<String>();

	public TagList(String... strings) {
		m_tags.addAll(Arrays.asList(strings));
	}

	public TagList(List<String> strings) {
		if (strings == null)
			throw new TagException("Cannot initialize a TagList with null strings!");
		m_tags.addAll(strings);
	}

	public TagList(TagList tags) {
		if (tags == null)
			throw new TagException("Cannot initialize a TagList with a null TagList!");
		m_tags.addAll(tags.m_tags);
	}

	/**
	 * Returns a new TagList which equals this + tag.
	 * 
	 * @param tag
	 * @return
	 */
	public TagList newAdd(String tag) {
		if (tag == null)
			throw new TagException("Cannot add a null tag to a TagList!");
		List<String> newTags = new ArrayList<String>(m_tags);
		newTags.add(tag);
		return new TagList(newTags);
	}

	/**
	 * Returns a new TagList which equals this - tag.
	 * 
	 * @param tag
	 * @return
	 */
	public TagList newRemove(String tag) {
		if (tag == null)
			throw new TagException("Cannot remove a null tag from a TagList!");
		List<String> newTags = new ArrayList<String>(m_tags);
		if (!newTags.contains(tag))
			throw new TagException("Cannot remove nonexistant tag!");

		newTags.remove(tag);
		return new TagList(newTags);
	}

	@Override
	public Iterator<String> iterator() {
		return m_tags.iterator();
	}

	public boolean containsGroup(String group) {
		return m_tags.contains(group);
	}

	@SuppressWarnings("unchecked")
	public boolean intersects(TagList tags) {
		List<String> tagsClone = (List<String>) m_tags.clone();
		tagsClone.retainAll(tags.m_tags);
		return !tagsClone.isEmpty();
	}

	@Override
	public String toString() {
		return Arrays.toString(m_tags.toArray());
	}
}