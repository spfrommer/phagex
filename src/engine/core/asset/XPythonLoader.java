package engine.core.asset;

import commons.Resource;
import engine.core.script.XPython;

/**
 * Loads a string from a file as a script.
 */
public class XPythonLoader implements AssetLoader<XPython> {
	@Override
	public XPython load(Resource resource, Object[] params) {
		return new XPython(ResourceFactory.readString(resource));
	}
}
