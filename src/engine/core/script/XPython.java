package engine.core.script;

import org.python.core.Py;
import org.python.core.PyFloat;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import commons.Logger;
import commons.ResourceFactory;
import commons.ResourceLocator;

import engine.core.exceptions.XScriptException;

public class XPython extends XScript {
	private PythonInterpreter m_python;
	private PyObject m_sceneLoad;
	private PyObject m_update;
	private PyObject m_exit;

	private String m_code;

	public XPython(String code) {
		if (code == null)
			throw new XScriptException("Cannot init Script with null String!");
		m_code = code;
	}

	public XPython(ResourceLocator locator, String name) {
		if (locator == null || name == null)
			throw new XScriptException("Cannot init Script with null resources!");
		m_code = ResourceFactory.readString(locator, name);
	}

	@Override
	public void onContextSet() {
		m_python = new PythonInterpreter();

		XScriptContext context = context();

		m_python.set("game", context.game);
		m_python.set("scene", context.scene);
		m_python.set("entity", context.entity);
		m_python.set("logger", Logger.instance());

		m_python.exec(m_code);

		m_sceneLoad = m_python.get("onSceneLoad");
		m_update = m_python.get(XScript.UPDATE);
		m_exit = m_python.get(XScript.EXIT);
	}

	@Override
	public void onSceneLoad() {
		m_sceneLoad.__call__();
	}

	@Override
	public void update(float time) {
		m_update.__call__(new PyFloat(time));
	}

	@Override
	public void exit() {
		m_exit.__call__();
	}

	@Override
	public void callFunc(String func, Object[] params) {
		if (func == null)
			throw new XScriptException("Cannot call a null function!");
		if (params == null)
			throw new XScriptException("Cannot call a function with null params!");

		try {
			PyObject function = m_python.get(func);
			if (params.length == 0) {
				function.__call__();
			} else {
				PyObject[] pyParams = new PyObject[params.length];
				for (int i = 0; i < params.length; i++)
					pyParams[i] = Py.java2py(params[i]);

				function.__call__(pyParams);
			}
		} catch (Exception ex) {
			return;
		}
	}

	@Override
	public XPython duplicate() {
		return new XPython(m_code);
	}
}
