package engine.core.script;

import java.util.List;

import org.python.core.Py;
import org.python.core.PyFloat;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import commons.Logger;

import engine.core.Component;
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

	@Override
	public void onContextSet() {
		m_python = new PythonInterpreter();

		XScriptContext context = context();

		m_python.set("logger", Logger.instance());
		m_python.set("game", context.game);
		m_python.set("scene", context.scene);
		m_python.set("entity", context.entity);

		m_python.set("tree", context.entity.tree());
		m_python.set("scripts", context.entity.scripts());
		m_python.set("components", context.entity.components());

		List<Component> allComps = context.entity.components().all();
		for (Component c : allComps)
			m_python.set(c.getName(), c);
	}

	@Override
	public void init() {
		m_python.exec(m_code);

		m_sceneLoad = m_python.get(XScript.SCENE_LOAD);
		m_update = m_python.get(XScript.UPDATE);
		m_exit = m_python.get(XScript.EXIT);
	}

	@Override
	public void onSceneLoad() {
		if (m_sceneLoad != null)
			m_sceneLoad.__call__();
	}

	@Override
	public void update(float time) {
		if (m_update != null)
			m_update.__call__(new PyFloat(time));
	}

	@Override
	public void exit() {
		if (m_exit != null)
			m_exit.__call__();
	}

	@Override
	public void callFunc(String func, Object... params) {
		if (func == null)
			throw new XScriptException("Cannot call a null function!");
		if (params == null)
			throw new XScriptException("Cannot call a function with null params!");

		PyObject function = m_python.get(func);
		if (function == null)
			return;

		if (params.length == 0) {
			function.__call__();
		} else {
			PyObject[] pyParams = new PyObject[params.length];
			for (int i = 0; i < params.length; i++)
				pyParams[i] = Py.java2py(params[i]);

			function.__call__(pyParams);
		}
	}

	@Override
	public XPython duplicate() {
		return new XPython(m_code);
	}

	@Override
	public void addScriptObject(XScriptObject object) {
		m_python.set(object.getName(), object.getObject());
	}
}
