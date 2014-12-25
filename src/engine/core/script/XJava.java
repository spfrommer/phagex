package engine.core.script;

import java.util.List;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;
import bsh.UtilEvalError;

import commons.Logger;

import engine.core.Component;

public class XJava extends XScript {
	private Interpreter m_interpreter;

	private String m_code;

	public XJava(String code) {
		m_code = code;
	}

	@Override
	public void onContextSet() {
		m_interpreter = new Interpreter();
		m_interpreter.setStrictJava(false);

		XScriptContext context = context();

		try {
			NameSpace ns = m_interpreter.getNameSpace();
			ns.setVariable("logger", Logger.instance(), false);
			ns.setVariable("game", context.game, false);
			ns.setVariable("scene", context.scene, false);
			ns.setVariable("entity", context.entity, false);

			ns.setVariable("tree", context.entity.tree(), false);
			ns.setVariable("scripts", context.entity.scripts(), false);
			ns.setVariable("components", context.entity.components(), false);

			List<Component> allComps = context.entity.components().all();
			for (Component c : allComps)
				ns.setVariable(c.getName(), c, false);
		} catch (UtilEvalError e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		try {
			m_interpreter.eval(m_code);
		} catch (EvalError e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSceneLoad() {
		callFunc(XScript.SCENE_LOAD);
	}

	@Override
	public void update(float time) {
		callFunc(XScript.UPDATE, time);
	}

	@Override
	public void exit() {
		callFunc(XScript.EXIT);
	}

	@Override
	public void callFunc(String func, Object... params) {
		Class<?>[] argTypes = new Class<?>[params.length];
		for (int i = 0; i < params.length; i++) {
			argTypes[i] = params[i].getClass();
		}
		try {
			if (m_interpreter.getNameSpace().getMethod(func, argTypes) == null) {
				return;
			}
			m_interpreter.getNameSpace().invokeMethod(func, params, m_interpreter);
		} catch (EvalError e) {
			e.printStackTrace();
		} catch (UtilEvalError e) {
			e.printStackTrace();
		}
	}

	@Override
	public XScript duplicate() {
		return new XJava(m_code);
	}

	@Override
	public void addScriptObject(XScriptObject object) {
		try {
			m_interpreter.getNameSpace().setVariable(object.getName(), object.getObject(), false);
		} catch (UtilEvalError e) {
			e.printStackTrace();
		}
	}
}
