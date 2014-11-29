package engine.core.script;

import engine.core.exceptions.ScriptException;

/**
 * A Script which defines a behavior that the Entity has.
 */
public abstract class Script {
	private ScriptContext m_context;

	/**
	 * Initializes a Script with no ScriptContext.
	 */
	public Script() {

	}

	/**
	 * Nulls out the ScriptContext.
	 */
	public void deactivateContext() {
		m_context = null;
	}

	/**
	 * Sets the ScriptContext the Script is running in.
	 * 
	 * @param context
	 */
	public void setContext(ScriptContext context) {
		if (context == null)
			throw new ScriptException("Cannot have null context!");
		m_context = context;
	}

	/**
	 * @return the ScriptContext the Script is running in.
	 */
	public ScriptContext context() {
		return m_context;
	}

	/**
	 * Starts the Script.
	 */
	public abstract void onStart();

	/**
	 * Updates the Script.
	 * 
	 * @param time
	 */
	public abstract void update(float time);

	/**
	 * Makes the Script duplicate itself.
	 * 
	 * @return
	 */
	public abstract Script duplicate();
}
