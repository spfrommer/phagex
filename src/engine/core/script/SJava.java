package engine.core.script;

import engine.core.CTransform;
import engine.core.Entity;
import engine.core.TagList;

/**
 * A Script written in Java.
 */
public class SJava extends Script {
	@Override
	public void update(float time) {
		CTransform trans = (CTransform) context().entity.components().getComponent(CTransform.NAME);
		System.out.println("Got trans: " + trans.getTransform());

		Entity child1 = context().entity.tree().getChild("child");
		TagList tags = child1.getTags();
		System.out.println(tags);
	}

	@Override
	public void onStart() {

	}

	@Override
	public Script duplicate() {
		return new SJava();
	}
}
