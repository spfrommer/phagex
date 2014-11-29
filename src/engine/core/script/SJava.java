package engine.core.script;

/**
 * A Script written in Java.
 */
public class SJava extends Script {
	@Override
	public void update(float time) {
		System.out.println(this.context().entity.components().getField("translation"));
	}

	@Override
	public void onStart() {

	}

	@Override
	public Script duplicate() {
		return new SJava();
	}
}
