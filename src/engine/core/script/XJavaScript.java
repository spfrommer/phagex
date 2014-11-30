package engine.core.script;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import commons.Logger;
import commons.ResourceFactory;
import commons.ResourceLocator;

import engine.core.exceptions.XScriptException;

/**
 * A JavaScript script
 */
public class XJavaScript extends XScript {
	private Context m_jsContext;
	@SuppressWarnings("unused")
	private Scriptable m_jsScope;

	private String m_code;

	public XJavaScript(String code) {
		m_code = code;
	}

	public XJavaScript(ResourceLocator locator, String name) {
		m_code = ResourceFactory.readString(locator, name);
	}

	@Override
	public void onContextSet() {
		m_jsContext = Context.enter();
		m_jsScope = makeScope(m_jsContext);
	}

	private Scriptable makeScope(Context context) {
		XScriptContext c = this.context();

		Scriptable scope = new ImporterTopLevel(context);

		Object logger = Context.javaToJS(Logger.instance(), scope);
		ScriptableObject.putProperty(scope, "logger", logger);

		Object entity = Context.javaToJS(c.entity, scope);
		ScriptableObject.putProperty(scope, "entity", entity);

		Object scene = Context.javaToJS(c.scene, scope);
		ScriptableObject.putProperty(scope, "scene", scene);

		Object game = Context.javaToJS(c.game, scope);
		ScriptableObject.putProperty(scope, "game", game);

		return scope;
	}

	@Override
	public void onSceneLoad() {
		// call scene load function
	}

	@Override
	public void update(float time) {
		// call update function
	}

	@Override
	public void exit() {
		// call exit function
		Context.exit();
	}

	@Override
	public void callFunc(String func, Object[] params) {
		if (func == null)
			throw new XScriptException("Cannot call a null function!");
		if (params == null)
			throw new XScriptException("Cannot call a function with null params!");
		// call function
	}

	@Override
	public XJavaScript duplicate() {
		return new XJavaScript(m_code);
	}
}
