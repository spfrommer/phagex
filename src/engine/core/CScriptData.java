package engine.core;

import java.util.HashMap;
import java.util.Map;

import engine.core.exceptions.ComponentException;

/**
 * Contains the shared data for all the Scripts on the Entity.
 */
public class CScriptData implements Component {
	public static final String NAME = "script_data";
	private Map<String, Object> m_data = new HashMap<String, Object>();

	public CScriptData() {
	}

	@Override
	public String getName() {
		return NAME;
	}

	public String[] getIdentifiers() {
		return m_data.keySet().toArray(new String[0]);
	}

	public Object getData(String identifier) {
		if (identifier == null)
			throw new ComponentException("Cannot get script data with a null identifier");
		if (!m_data.containsKey(identifier))
			throw new ComponentException("No script data for identifier: " + identifier);

		return m_data.get(identifier);
	}

	public void setData(String identifier, Object data) {
		if (identifier == null)
			throw new ComponentException("Cannot set script data with a null identifier");
		if (data == null)
			throw new ComponentException("Cannot set null data for identifier: " + identifier);

		m_data.put(identifier, data);
	}

	/**
	 * Returns a Builder for an empty CScriptData.
	 */
	@Override
	public ComponentBuilder<CScriptData> getBuilder() {
		ComponentBuilder<CScriptData> builder = new ComponentBuilder<CScriptData>() {
			@Override
			public CScriptData build() {
				return new CScriptData();
			}

			@Override
			public String getName() {
				return NAME;
			}
		};
		return builder;
	}
}
