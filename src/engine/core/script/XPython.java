package engine.core.script;

import org.python.core.PyFloat;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import commons.Logger;
import commons.ResourceFactory;
import commons.ResourceLocator;

public class XPython extends XScript {
	private PythonInterpreter m_python;
	private PyObject m_sceneLoad;
	private PyObject m_update;
	private PyObject m_exit;

	private String m_code;

	public XPython(String code) {
		m_code = code;
	}

	public XPython(ResourceLocator locator, String name) {
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
	public XPython duplicate() {
		return new XPython(m_code);
	}
}
