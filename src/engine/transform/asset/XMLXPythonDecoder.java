package engine.transform.asset;

import org.jsoup.nodes.Element;

import commons.Resource;
import commons.ResourceLocator;

import engine.core.asset.AssetLoader;
import engine.core.exceptions.AssetException;
import engine.core.script.XPython;

/**
 * Decodes a XPython script.
 */
public class XMLXPythonDecoder extends XMLAssetDecoder<XPython> {
	private AssetLoader<XPython> m_loader;

	public XMLXPythonDecoder(AssetLoader<XPython> loader) {
		m_loader = loader;
	}

	@Override
	public XPython transform(Element element) {
		if (!element.hasAttr("path"))
			throw new AssetException("No path attribute for XPython!");

		ResourceLocator locator = getLocator();

		Resource path = new Resource(locator, element.attr("path"));

		return m_loader.load(path, new Object[0]);
	}
}
