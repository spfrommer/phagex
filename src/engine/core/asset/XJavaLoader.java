package engine.core.asset;

import commons.Resource;

import engine.core.script.XJava;

public class XJavaLoader implements AssetLoader<XJava> {
	@Override
	public XJava load(Resource resource, Object[] params) {
		return new XJava(ResourceFactory.readString(resource));
	}
}
