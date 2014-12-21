package engine.core.asset;

import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import commons.Logger;
import commons.Resource;
import commons.ResourceLocator;
import commons.ResourceLocator.ClasspathResourceLocator;

import engine.core.exceptions.AssetException;
import engine.core.script.XJava;
import engine.core.script.XPython;
import engine.imp.render.Animation;
import engine.imp.render.Material2D;
import engine.transform.asset.StringReader;
import engine.transform.asset.XMLAnimationDecoder;
import engine.transform.asset.XMLAssetDecoder;
import engine.transform.asset.XMLMaterialDecoder;
import engine.transform.asset.XMLMaterialParamsDecoder;
import engine.transform.asset.XMLXJavaDecoder;
import engine.transform.asset.XMLXPythonDecoder;
import gltools.gl.GL;

/**
 * Manages the assets of a game.
 */
public class AssetManager {
	// maps the type to the asset loaders
	private HashMap<Class<? extends Asset>, AssetLoader<? extends Asset>> m_loaders = new HashMap<Class<? extends Asset>, AssetLoader<? extends Asset>>();
	// maps the loader to its XMLAssetDecoders
	private HashMap<Class<? extends Asset>, XMLAssetDecoder<? extends Asset>> m_xmlDecoders = new HashMap<Class<? extends Asset>, XMLAssetDecoder<? extends Asset>>();
	// maps the Resource identifier to the Resource
	private HashMap<String, Asset> m_assets = new HashMap<String, Asset>();

	private AssetManager(GL gl) {
		StringReader reader = new StringReader();

		MaterialLoader matLoader = new MaterialLoader(gl);
		XMLMaterialDecoder matDecoder = new XMLMaterialDecoder(matLoader, new XMLMaterialParamsDecoder());
		AnimationLoader animationLoader = new AnimationLoader(reader, matDecoder);
		XPythonLoader pythonLoader = new XPythonLoader(reader);
		XJavaLoader javaLoader = new XJavaLoader(reader);

		m_loaders.put(Material2D.class, matLoader);
		m_loaders.put(Animation.class, animationLoader);
		m_loaders.put(XPython.class, pythonLoader);
		m_loaders.put(XJava.class, javaLoader);

		m_xmlDecoders.put(Material2D.class, matDecoder);
		m_xmlDecoders.put(Animation.class, new XMLAnimationDecoder(animationLoader));
		m_xmlDecoders.put(XPython.class, new XMLXPythonDecoder(pythonLoader));
		m_xmlDecoders.put(XJava.class, new XMLXJavaDecoder(javaLoader));
	}

	/**
	 * Adds a loader for a specific resource type.
	 * 
	 * @param type
	 * @param loader
	 */
	public <T extends Asset> void addLoader(Class<T> type, AssetLoader<T> loader) {
		if (m_loaders.containsKey(type))
			throw new AssetException("Cannot add two loaders for the same resource type!");
		m_loaders.put(type, loader);
	}

	/**
	 * Loads a Resource.
	 * 
	 * @param identifier
	 * @param resource
	 * @param type
	 * @param params
	 */
	public void load(String identifier, Resource resource, Class<? extends Asset> type, Object... params) {
		if (identifier == null)
			throw new AssetException("Can't have null identifier for assets!");
		if (resource == null)
			throw new AssetException("Can't have null Resources!");
		if (type == null)
			throw new AssetException("Can't have null identifier for types!");
		if (m_assets.containsKey(identifier))
			throw new AssetException("Resource is already loaded for: " + identifier);
		if (!m_loaders.containsKey(type))
			throw new AssetException("No loader defined for type: " + type);

		Asset asset = m_loaders.get(type).load(resource, (params == null) ? new Object[0] : params);
		asset.setIdentifier(identifier);

		m_assets.put(identifier, asset);
	}

	/**
	 * Loads from xml using the xml decoders.
	 * 
	 * @param identifier
	 * @param element
	 * @param locator
	 * @param type
	 */
	private void loadXML(String identifier, Element element, ResourceLocator locator, Class<? extends Asset> type) {
		XMLAssetDecoder<? extends Asset> decoder = m_xmlDecoders.get(type);
		decoder.setLocator(locator);

		Asset asset = decoder.transform(element);
		asset.setIdentifier(identifier);

		m_assets.put(identifier, asset);
	}

	/**
	 * Loads all the resources in the given XML file.
	 * 
	 * @param file
	 */
	public void loadFromFile(Resource file) {
		String xml = new StringReader().transform(file);
		Document doc = Jsoup.parse(xml, "", Parser.xmlParser());

		Element assets = doc.getElementsByTag("assets").first();
		Elements classpaths = assets.getElementsByTag("classpath");
		ClasspathResourceLocator classpath = new ClasspathResourceLocator();
		for (Element e : classpaths) {
			Elements children = e.children();
			for (Element child : children) {
				if (!child.hasAttr("name"))
					throw new AssetException("No name attribute!");
				String type = child.tagName();
				String name = child.attr("name");

				Class<? extends Asset> assetType = null;
				switch (type) {
				case "material":
					assetType = Material2D.class;
					break;
				case "animation":
					assetType = Animation.class;
					break;
				case "xpython":
					assetType = XPython.class;
					break;
				case "xjava":
					assetType = XJava.class;
					break;
				default:
					Logger.instance().error("Did not recognize type: " + type);
				}

				loadXML(name, child, classpath, assetType);
			}
		}
	}

	/**
	 * Gets the asset for the identifier.
	 * 
	 * @param identifier
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Asset> T get(String identifier, Class<T> c) {
		if (!m_assets.containsKey(identifier))
			throw new AssetException("No resource for identifier: " + identifier);

		Asset asset = m_assets.get(identifier);
		return (T) asset;
	}

	/**
	 * Gets the asset for the identifier.
	 * 
	 * @param identifier
	 * @return
	 */
	public Asset get(String identifier) {
		if (!m_assets.containsKey(identifier))
			throw new AssetException("No resource for identifier: " + identifier);

		Asset asset = m_assets.get(identifier);
		return asset;
	}

	/**
	 * Test prints all the assets.
	 */
	public void dump() {
		for (String s : m_assets.keySet()) {
			Logger.instance().out(s);
		}
	}

	private static AssetManager INSTANCE;

	public static AssetManager instance() {
		if (INSTANCE == null)
			throw new AssetException("AssetManager has not been initialized!");
		return INSTANCE;
	}

	public static AssetManager init(GL gl) {
		INSTANCE = new AssetManager(gl);
		return INSTANCE;
	}
}
