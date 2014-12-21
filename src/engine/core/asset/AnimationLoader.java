package engine.core.asset;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import commons.Logger;
import commons.Resource;
import commons.ResourceLocator;

import engine.imp.render.Animation;
import engine.imp.render.Frame;
import engine.imp.render.Material2D;
import engine.transform.Transformer;
import engine.transform.asset.XMLAssetDecoder;

/**
 * Loads an animation from a file.
 */
public class AnimationLoader implements AssetLoader<Animation> {
	private Transformer<Resource, String> m_transformer;
	private XMLAssetDecoder<Material2D> m_materialDecoder;

	public AnimationLoader(Transformer<Resource, String> transformer, XMLAssetDecoder<Material2D> materialDecoder) {
		m_transformer = transformer;
		m_materialDecoder = materialDecoder;
	}

	@Override
	public Animation load(Resource resource, Object[] params) {
		String string = m_transformer.transform(resource);
		Document doc = Jsoup.parse(string, "", Parser.xmlParser());

		Element animation = doc.getElementsByTag("animation").first();
		return readAnimation(animation, resource.getLocator());
	}

	private Animation readAnimation(Element animation, ResourceLocator locator) {
		List<Frame> frameList = new ArrayList<Frame>();
		boolean repeat = false;

		if (animation.hasAttr("repeat")) {
			String repeatAttr = animation.attr("repeat");
			if (repeatAttr.equals("true")) {
				repeat = true;
			} else if (repeatAttr.equals("false")) {
				repeat = false;
			} else {
				Logger.instance().error("Not a legal repeat flag: " + repeatAttr);
			}
		}

		Elements frames = animation.getElementsByTag("frame");
		for (Element e : frames) {
			Element material = e.getElementsByTag("material").first();
			float duration = Float.parseFloat(e.attr("duration"));
			m_materialDecoder.setLocator(locator);
			Material2D mat2d = m_materialDecoder.transform(material);

			frameList.add(new Frame(mat2d, duration));
		}

		Animation anim = new Animation(frameList);
		anim.setRepeating(repeat);

		return anim;
	}
}
