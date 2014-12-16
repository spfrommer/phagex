package commons;

import java.io.InputStream;

/**
 * A Resource.
 */
public class Resource {
	private ResourceLocator m_locator;
	private String m_resource;

	/**
	 * Creates a Resource.
	 * 
	 * @param locator
	 * @param resource
	 */
	public Resource(ResourceLocator locator, String resource) {
		if (locator == null)
			throw new ResourceException("Null locator for Resource!");
		if (resource == null)
			throw new ResourceException("Null resource string!");

		m_locator = locator;
		m_resource = resource;
	}

	/**
	 * Returns the locator.
	 * 
	 * @return
	 */
	public ResourceLocator getLocator() {
		return m_locator;
	}

	/**
	 * Returns the resource.
	 * 
	 * @return
	 */
	public String getResource() {
		return m_resource;
	}

	/**
	 * Gets an InputStream to the Resource.
	 * 
	 * @return
	 */
	public InputStream open() {
		return m_locator.getResource(m_resource);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_locator == null) ? 0 : m_locator.hashCode());
		result = prime * result + ((m_resource == null) ? 0 : m_resource.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Resource other = (Resource) obj;
		if (m_locator == null) {
			if (other.m_locator != null)
				return false;
		} else if (!m_locator.equals(other.m_locator))
			return false;
		if (m_resource == null) {
			if (other.m_resource != null)
				return false;
		} else if (!m_resource.equals(other.m_resource))
			return false;
		return true;
	}
}
