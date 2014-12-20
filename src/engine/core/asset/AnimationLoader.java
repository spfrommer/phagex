package engine.core.asset;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import commons.Resource;

import engine.imp.render.Animation;

/**
 * Loads an animation from a file.
 */
public class AnimationLoader implements AssetLoader<Animation> {
	private MaterialLoader m_materialLoader;

	public AnimationLoader(MaterialLoader materialLoader) {
		m_materialLoader = materialLoader;
	}

	@Override
	public Animation load(Resource resource, Object[] params) {
		String string = ResourceFactory.readString(resource);
		Document doc = Jsoup.parse(string, "", Parser.xmlParser());

		Element animation = doc.getElementsByTag("animation").first();
		return ResourceFactory.readAnimation(animation, m_materialLoader, resource.getLocator());
	}
}