package engine.transform.asset;

import org.jsoup.nodes.Element;

import commons.Resource;
import commons.ResourceLocator;

import engine.core.asset.AssetLoader;
import engine.core.asset.MaterialParams;
import engine.core.exceptions.AssetException;
import engine.imp.render.Material2D;

/**
 * Decodes a Material2D.
 */
public class XMLMaterialDecoder extends XMLAssetDecoder<Material2D> {
	private AssetLoader<Material2D> m_loader;
	private XMLAssetDecoder<MaterialParams> m_paramsDecoder;

	public XMLMaterialDecoder(AssetLoader<Material2D> loader, XMLAssetDecoder<MaterialParams> paramsDecoder) {
		m_loader = loader;
		m_paramsDecoder = paramsDecoder;
	}

	@Override
	public void setLocator(ResourceLocator locator) {
		super.setLocator(locator);
		m_paramsDecoder.setLocator(locator);
	}

	@Override
	public Material2D transform(Element element) {
		if (!element.hasAttr("texture"))
			throw new AssetException("No texture attribute for material!");

		ResourceLocator locator = getLocator();

		Resource texture = new Resource(locator, element.attr("texture"));

		return m_loader.load(texture, new Object[] { m_paramsDecoder.transform(element) });
	}
}
