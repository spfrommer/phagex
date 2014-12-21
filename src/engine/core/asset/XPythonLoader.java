package engine.core.asset;

import commons.Resource;

import engine.core.script.XPython;
import engine.transform.Transformer;

/**
 * Loads a string from a file as a script.
 */
public class XPythonLoader implements AssetLoader<XPython> {
	private Transformer<Resource, String> m_transformer;

	public XPythonLoader(Transformer<Resource, String> transformer) {
		m_transformer = transformer;
	}

	@Override
	public XPython load(Resource resource, Object[] params) {
		return new XPython(m_transformer.transform(resource));
	}
}
