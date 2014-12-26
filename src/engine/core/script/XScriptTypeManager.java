package engine.core.script;

import java.util.HashMap;

public class XScriptTypeManager {
	private static XScriptTypeManager s_manager;
	
	private HashMap<String, XScriptFactory> m_factories = new HashMap<String, XScriptFactory>();
	private HashMap<Class<? extends XScript>, String> m_types = new HashMap<Class<? extends XScript>, String>();
	
	
	public String getType(Class<? extends XScript> clazz) {
		return m_types.get(clazz);
	}
	public XScriptFactory getFactory(String type) {
		return m_factories.get(type);
	}
	
	public void registerType(Class<? extends XScript> clazz, String typeStr, XScriptFactory factory) {
		m_types.put(clazz, typeStr);
		m_factories.put(typeStr, factory);
	}
	
	public static XScriptTypeManager instance() {
		if (s_manager == null) s_manager = new XScriptTypeManager();
		return s_manager;
	}
}
