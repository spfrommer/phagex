package commons;

import java.io.InputStream;

/**
 * A wrapper class for the gltools ResourceLocator.
 */
public class GLResourceLocator implements glcommon.util.ResourceLocator {
	private ResourceLocator m_locator;

	/**
	 * Creates the wrapper.
	 * 
	 * @param locator
	 */
	public GLResourceLocator(ResourceLocator locator) {
		m_locator = locator;
	}

	@Override
	public InputStream getResource(String resource) {
		return m_locator.getResource(resource);
	}
}
