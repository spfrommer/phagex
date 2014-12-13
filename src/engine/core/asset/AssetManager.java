package engine.core.asset;

import java.util.HashMap;
import java.util.Map;

import commons.Resource;

import engine.core.exceptions.AssetException;

/**
 * Holds the Assets of the game as a singleton object.
 */
public class AssetManager {
	private Map<String, Asset> m_assets = new HashMap<String, Asset>();

	private AssetManager() {

	}

	/**
	 * Defines an Asset.
	 * 
	 * @param name
	 * @param type
	 * @param resource
	 * @param asset
	 */
	public void defineAsset(String name, AssetType type, Resource resource, Object asset) {
		if (name == null)
			throw new AssetException("Cannot set an Asset with a null name!");
		if (m_assets.containsKey(asset))
			throw new AssetException("Asset already defined for: " + name);

		m_assets.put(name, new Asset(resource, asset, type));
	}

	/**
	 * @param asset
	 * @return the Asset associated with the name
	 */
	public Asset getAsset(String asset) {
		if (asset == null)
			throw new AssetException("Cannot get an Asset with a null name!");
		if (!m_assets.containsKey(asset))
			throw new AssetException("No Asset for key: " + asset);

		return m_assets.get(asset);
	}

	private static AssetManager m_manager;

	public static AssetManager instance() {
		if (m_manager == null)
			m_manager = new AssetManager();

		return m_manager;
	}
}
