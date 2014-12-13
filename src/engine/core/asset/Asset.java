package engine.core.asset;

import commons.Resource;

import engine.core.exceptions.AssetException;

/**
 * Holds an Asset.
 */
public class Asset {

	private Resource m_resource;
	private Object m_asset;
	private AssetType m_type;

	/**
	 * Initailizes an Asset.
	 * 
	 * @param resource
	 * @param asset
	 * @param type
	 */
	protected Asset(Resource resource, Object asset, AssetType type) {
		if (resource == null)
			throw new AssetException("Cannot initialize Asset with null Resource!");
		if (asset == null)
			throw new AssetException("Cannot initialize Asset with null Asset!");
		if (type == null)
			throw new AssetException("Cannot initialize Asset with null Type!");
		m_resource = resource;
		m_asset = asset;
		m_type = type;
	}

	/**
	 * @return the Resource
	 */
	public Resource getResource() {
		return m_resource;
	}

	/**
	 * @return the Asset
	 */
	public Object getAsset() {
		return m_asset;
	}

	/**
	 * @return the Type
	 */
	public AssetType getType() {
		return m_type;
	}
}
