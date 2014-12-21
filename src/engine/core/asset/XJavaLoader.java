package engine.core.asset;

import commons.Resource;

import engine.core.script.XJava;
import engine.transform.Transformer;

public class XJavaLoader implements AssetLoader<XJava> {
	private Transformer<Resource, String> m_transformer;

	public XJavaLoader(Transformer<Resource, String> transformer) {
		m_transformer = transformer;
	}

	@Override
	public XJava load(Resource resource, Object[] params) {
		return new XJava(m_transformer.transform(resource));
	}
}
