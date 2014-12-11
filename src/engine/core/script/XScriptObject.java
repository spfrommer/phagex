package engine.core.script;

/**
 * An object that is injected into the Script.
 */
public class XScriptObject {
	private String m_name;
	private Object m_object;

	public XScriptObject(String name, Object object) {
		m_name = name;
		m_object = object;
	}

	/**
	 * @return what the field will be called in the Script
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * Sets what the field will be called in the Script.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		m_name = name;
	}

	/**
	 * @return the java Object
	 */
	public Object getObject() {
		return m_object;
	}

	/**
	 * Sets the java Object.
	 * 
	 * @param object
	 */
	public void setObject(Object object) {
		m_object = object;
	}
}
