package engine.transform.asset;

import org.jsoup.nodes.Element;

import commons.Resource;
import commons.ResourceLocator;

import engine.core.asset.AssetLoader;
import engine.core.exceptions.AssetException;
import engine.core.script.XJava;

/**
 * Decodes a XJava script.
 */
public class XMLXJavaDecoder extends XMLAssetDecoder<XJava> {
	private AssetLoader<XJava> m_loader;

	public XMLXJavaDecoder(AssetLoader<XJava> loader) {
		m_loader = loader;
	}

	@Override
	public XJava transform(Element element) {
		if (!element.hasAttr("path"))
			throw new AssetException("No path attribute for XJava!");

		ResourceLocator locator = getLocator();

		Resource path = new Resource(locator, element.attr("path"));

		return m_loader.load(path, new Object[0]);
	}
}
