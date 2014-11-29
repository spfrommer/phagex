package engine.core.script;

import commons.Transform2f;

import engine.core.CTransform;
import engine.core.Entity;

/**
 * A Script written in Java.
 */
public class SJava extends Script {
	@Override
	public void update(float time) {
		CTransform trans = (CTransform) context().entity.components().getComponent(CTransform.NAME);
		System.out.println("Got trans: " + trans.getTransform());

		Entity child1 = context().entity.tree().getChild("child");
		Transform2f ctrans = child1.getTransform();
		System.out.println("Child trans: " + ctrans);
	}

	@Override
	public void onStart() {

	}

	@Override
	public Script duplicate() {
		return new SJava();
	}
}
