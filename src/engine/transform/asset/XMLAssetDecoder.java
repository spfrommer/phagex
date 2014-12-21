package engine.transform.asset;

import org.jsoup.nodes.Element;

import commons.ResourceLocator;
import engine.transform.Transformer;

/**
 * Decodes an asset.
 * 
 * @param <T>
 */
public abstract class XMLAssetDecoder<T extends Object> implements Transformer<Element, T> {
	private ResourceLocator m_locator;

	/**
	 * @return the locator for finding the asset
	 */
	public ResourceLocator getLocator() {
		return m_locator;
	}

	/**
	 * Sets the locator for finding the asset
	 * 
	 * @param locator
	 */
	public void setLocator(ResourceLocator locator) {
		m_locator = locator;
	}
}
