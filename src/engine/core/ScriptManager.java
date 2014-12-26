package engine.core;

import java.util.ArrayList;
import java.util.List;

import engine.core.exceptions.XScriptException;
import engine.core.script.XScript;
import engine.core.script.XScriptContext;

/**
 * Manages the Scripts in an Entity.
 */
public class ScriptManager {
	private Entity m_entity;
	private List<XScript> m_scripts;

	// the context the scripts are running under
	private XScriptContext m_context;

	public ScriptManager(Entity entity) {
		m_scripts = new ArrayList<XScript>();

		Scene scene = entity.getScene();

		m_context = new XScriptContext(scene.getGame(), scene, entity);
		m_entity = entity;
	}

	/**
	 * Called when the Game starts.
	 */
	protected void onSceneLoad() {
		for (XScript script : m_scripts)
			script.onSceneLoad();
	}

	/**
	 * Updates the Scripts.
	 * 
	 * @param time
	 */
	protected void update(float time) {
		for (XScript script : m_scripts)
			script.update(time);
	}

	/**
	 * Adds a Script to the ScriptManager.
	 * 
	 * @param script
	 */
	public void add(XScript script) {
		if (script == null)
			throw new XScriptException("Trying to add null Script!");
		if (m_scripts.contains(script))
			throw new XScriptException("Cannot add same Script twice!");

		script.setContext(m_context);
		m_scripts.add(script);
		m_entity.getScene().getGame().scriptAdded(m_entity, script, m_entity.getScene());
		script.init();
	}

	/**
	 * Removes a Script from the ScriptManager.
	 * 
	 * @param script
	 */
	public void remove(XScript script) {
		if (script == null)
			throw new XScriptException("Trying to remove a null Script!");
		if (!m_scripts.contains(script))
			throw new XScriptException("Trying to remove nonexistant Script!");

		script.exit();
		script.deactivateContext();
		m_scripts.remove(script);
	}
	/**
	 * Returns all scripts
	 * @return all scripts of the manager
	 */
	public List<XScript> getAllScripts() {
		return m_scripts;
	}
	
	/**
	 * Calls the function on all the Scripts.
	 * 
	 * @param func
	 * @param params
	 */
	public void callFunc(String func, Object... params) {
		if (func == null)
			throw new XScriptException("Cannot call a null function!");
		if (params == null)
			throw new XScriptException("Cannot call a function with null params!");

		for (XScript script : m_scripts)
			script.callFunc(func, params);
	}

	/**
	 * Calls the function on all the Scripts.
	 * 
	 * @param func
	 */
	public void callFunc(String func) {
		if (func == null)
			throw new XScriptException("Cannot call a null function!");

		Object[] params = new Object[0];
		for (XScript script : m_scripts)
			script.callFunc(func, params);
	}
}