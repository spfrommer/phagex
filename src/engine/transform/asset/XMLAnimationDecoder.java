package engine.transform.asset;

import org.jsoup.nodes.Element;

import commons.Resource;
import commons.ResourceLocator;

import engine.core.asset.AssetLoader;
import engine.core.exceptions.AssetException;
import engine.imp.render.Animation;

/**
 * Decodes an animation.
 */
public class XMLAnimationDecoder extends XMLAssetDecoder<Animation> {
	private AssetLoader<Animation> m_loader;

	public XMLAnimationDecoder(AssetLoader<Animation> loader) {
		m_loader = loader;
	}

	@Override
	public Animation transform(Element element) {
		if (!element.hasAttr("path"))
			throw new AssetException("No path attribute for animation!");

		ResourceLocator locator = getLocator();

		Resource path = new Resource(locator, element.attr("path"));

		return m_loader.load(path, new Object[0]);
	}
}
