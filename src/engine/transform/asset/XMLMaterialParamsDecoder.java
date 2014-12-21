package engine.transform.asset;

import org.jsoup.nodes.Element;

import commons.Resource;
import commons.ResourceLocator;

import engine.core.asset.MaterialParams;
import engine.core.exceptions.AssetException;
import glcommon.Color;

/**
 * Transforms an Element into a MaterialParams.
 */
public class XMLMaterialParamsDecoder extends XMLAssetDecoder<MaterialParams> {
	public XMLMaterialParamsDecoder() {
	}

	@Override
	public MaterialParams transform(Element element) {
		ResourceLocator locator = getLocator();
		MaterialParams params = new MaterialParams();

		if (element.hasAttr("normalmap")) {
			String normal = element.attr("normalmap");
			params.normalMap = new Resource(locator, normal);
		}
		if (element.hasAttr("color")) {
			String color = element.attr("color");
			String noWhitespace = color.replaceAll("\\s+", "");
			String[] rgb = noWhitespace.split(",");
			if (rgb.length != 3)
				throw new AssetException("A color must have 3 components: " + color);
			params.color = new Color(Float.parseFloat(rgb[0]), Float.parseFloat(rgb[1]), Float.parseFloat(rgb[2]));
		}
		if (element.hasAttr("lighted")) {
			String lighted = element.attr("lighted");
			if (lighted.equals("true")) {
				params.isLighted = true;
			} else if (lighted.equals("false")) {
				params.isLighted = false;
			} else {
				throw new AssetException("Lighted must be true or false: " + lighted);
			}
		}
		return params;
	}
}
