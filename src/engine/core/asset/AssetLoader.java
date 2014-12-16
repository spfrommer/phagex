package engine.core.asset;

import commons.Resource;

/**
 * Loads an asset from a resource.
 * 
 * @param <T>
 */
public interface AssetLoader<T> {
	/**
	 * Loads a resource.
	 * 
	 * @param resource
	 * @param params
	 *            the parameters to load with - this will never be null
	 * @return
	 */
	public T load(Resource resource, Object[] params);
}
