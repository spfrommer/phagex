package engine.core;

import java.util.ArrayList;
import java.util.List;

import engine.core.exceptions.ScriptException;
import engine.core.script.Script;
import engine.core.script.ScriptContext;

/**
 * Manages the Scripts in an Entity.
 */
public class ScriptManager {
	private List<Script> m_scripts;

	// the context the scripts are running under
	private ScriptContext m_context;

	public ScriptManager(Entity entity) {
		m_scripts = new ArrayList<Script>();

		Scene scene = entity.getScene();

		m_context = new ScriptContext(scene.getGame(), scene, entity);
	}

	/**
	 * Called when the Game starts.
	 */
	protected void onSceneLoad() {
		for (Script script : m_scripts)
			script.onStart();
	}

	/**
	 * Updates the Scripts.
	 * 
	 * @param time
	 */
	protected void update(float time) {
		for (Script script : m_scripts)
			script.update(time);
	}

	/**
	 * Adds a Script to the ScriptManager.
	 * 
	 * @param script
	 */
	public void add(Script script) {
		if (script == null)
			throw new ScriptException("Trying to add null Script!");
		if (m_scripts.contains(script))
			throw new ScriptException("Cannot add same Script twice!");

		script.setContext(m_context);
		m_scripts.add(script);
	}

	/**
	 * Removes a Script from the ScriptManager.
	 * 
	 * @param script
	 */
	public void remove(Script script) {
		if (script == null)
			throw new ScriptException("Trying to remove a null Script!");
		if (!m_scripts.contains(script))
			throw new ScriptException("Trying to remove nonexistant Script!");

		script.deactivateContext();
		m_scripts.remove(script);
	}
}
