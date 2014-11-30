package engine.core.script;

import engine.core.exceptions.XScriptException;

/**
 * A Script which defines a behavior that the Entity has.
 */
public abstract class XScript {
	public static final String SCENE_LOAD = "onSceneLoad";
	public static final String UPDATE = "update";
	public static final String EXIT = "exit";

	private XScriptContext m_context;

	/**
	 * Initializes a Script with no ScriptContext.
	 */
	public XScript() {
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
	public void setContext(XScriptContext context) {
		if (context == null)
			throw new XScriptException("Cannot have null context!");

		m_context = context;
		onContextSet();
	}

	/**
	 * @return the ScriptContext the Script is running in.
	 */
	public XScriptContext context() {
		return m_context;
	}

	/**
	 * Called when the ScriptContext is set.
	 */
	public abstract void onContextSet();

	/**
	 * Called when the Scene this Entity is a part of is made the main Scene of the game.
	 */
	public abstract void onSceneLoad();

	/**
	 * Updates the Script.
	 * 
	 * @param time
	 */
	public abstract void update(float time);

	/**
	 * Tells the Script to exit.
	 */
	public abstract void exit();

	/**
	 * Calls a function in the Script. If the function doesn't exist, the script simply ignores it.
	 * 
	 * @param func
	 * @param params
	 */
	public abstract void callFunc(String func, Object... params);

	/**
	 * Makes the Script duplicate itself.
	 * 
	 * @return
	 */
	public abstract XScript duplicate();
}
