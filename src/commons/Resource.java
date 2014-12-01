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
}
