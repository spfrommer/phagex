package engine.core.asset;

import engine.core.exceptions.AssetException;
import engine.core.script.XJava;
import engine.core.script.XPython;
import engine.imp.render.Animation;
import engine.imp.render.Material2D;
import gltools.gl.GL;

import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import commons.Logger;
import commons.Resource;
import commons.ResourceLocator.ClasspathResourceLocator;

/**
 * Manages the assets of a game.
 */
public class AssetManager {
	// maps the type to the asset loaders
	private HashMap<Class<?>, AssetLoader<?>> m_loaders = new HashMap<Class<?>, AssetLoader<?>>();
	// maps the Resource identifier to the Resource
	private HashMap<String, Resource> m_resourceIdentifiers = new HashMap<String, Resource>();
	// maps a Resource to its type
	private HashMap<Resource, Class<?>> m_resourceTypes = new HashMap<Resource, Class<?>>();
	// maps a Resource to the Resource Object
	private HashMap<Resource, Object> m_resourceObjects = new HashMap<Resource, Object>();

	private AssetManager(GL gl) {
		MaterialLoader loader = new MaterialLoader(gl);
		m_loaders.put(Material2D.class, loader);
		m_loaders.put(Animation.class, new AnimationLoader(loader));
		m_loaders.put(XPython.class, new XPythonLoader());
		m_loaders.put(XJava.class, new XJavaLoader());
	}

	/**
	 * Adds a loader for a specific resource type.
	 * 
	 * @param type
	 * @param loader
	 */
	public <T> void addLoader(Class<T> type, AssetLoader<T> loader) {
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
	public void load(String identifier, Resource resource, Class<?> type, Object... params) {
		if (identifier == null)
			throw new AssetException("Can't have null identifier for assets!");
		if (resource == null)
			throw new AssetException("Can't have null Resources!");
		if (type == null)
			throw new AssetException("Can't have null identifier for types!");
		if (m_resourceIdentifiers.containsKey(identifier))
			throw new AssetException("Resource is already loaded for: " + identifier);
		if (!m_loaders.containsKey(type))
			throw new AssetException("No loader defined for type: " + type);

		m_resourceIdentifiers.put(identifier, resource);
		m_resourceTypes.put(resource, type);
		m_resourceObjects.put(resource, m_loaders.get(type).load(resource, (params == null) ? new Object[0] : params));
	}

	/**
	 * Loads all the resources in the given XML file.
	 * 
	 * @param file
	 */
	public void loadFromFile(Resource file) {
		String xml = ResourceFactory.readString(file);
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

				if (type.equals("material")) {
					Resource resource = new Resource(classpath, child.attr("texture"));
					load(name, resource, Material2D.class, ResourceFactory.readMaterialParams(child, classpath));
				} else if (type.equals("animation")) {
					Resource resource = new Resource(classpath, child.attr("path"));
					load(name, resource, Animation.class);
				} else if (type.equals("xpython")) {
					Resource resource = new Resource(classpath, child.attr("path"));
					load(name, resource, XPython.class);
				} else if (type.equals("xjava")) {
					Resource resource = new Resource(classpath, child.attr("path"));
					load(name, resource, XJava.class);
				} else {
					Logger.instance().error("Did not recognize type: " + type);
				}
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
	public <T> T get(String identifier, Class<T> c) {
		if (!m_resourceIdentifiers.containsKey(identifier))
			throw new AssetException("No resource for identifier: " + identifier);
		if (!m_resourceTypes.get(m_resourceIdentifiers.get(identifier)).equals(c))
			throw new AssetException("Not of right type: " + identifier);

		Resource resource = m_resourceIdentifiers.get(identifier);
		Object resObject = m_resourceObjects.get(resource);
		return (T) resObject;
	}

	/**
	 * Gets the asset for the identifier.
	 * 
	 * @param identifier
	 * @return
	 */
	public Object get(String identifier) {
		if (!m_resourceIdentifiers.containsKey(identifier))
			throw new AssetException("No resource for identifier: " + identifier);

		Resource resource = m_resourceIdentifiers.get(identifier);
		Object resObject = m_resourceObjects.get(resource);
		return resObject;
	}

	/**
	 * Test prints all the assets.
	 */
	public void dump() {
		for (String s : m_resourceIdentifiers.keySet()) {
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
