package engine.imp.render;

import commons.Logger;

import engine.core.Entity;
import engine.core.EntityFilter;
import engine.core.EntitySystem;
import engine.core.Scene;
import engine.core.SimpleEntityFilter;
import engine.core.TreeNode;
import engine.core.script.XScript;

/**
 * Updates animations.
 */
public class AnimationSystem implements EntitySystem {
	private static final SimpleEntityFilter s_filter = new SimpleEntityFilter(new String[] { CRender.NAME, CAnimation.NAME },
			new String[0], false);

	@Override
	public void sceneChanged(Scene oldScene, Scene newScene) {
	}

	@Override
	public void entityAdded(Entity entity, TreeNode parent, Scene scene) {
	}

	@Override
	public void entityRemoved(Entity entity, TreeNode parent, Scene scene) {
	}

	@Override
	public void entityMoved(Entity entity, TreeNode oldParent, TreeNode newParent, Scene scene) {
	}

	@Override
	public void entityEnabled(Entity entity, TreeNode parent, Scene scene) {
	}

	@Override
	public void entityDisabled(Entity entity, TreeNode parent, Scene scene) {
	}

	@Override
	public void scriptAdded(Entity entity, XScript script, Scene scene) {
	}

	@Override
	public void update(Scene scene, float time) {
	}

	@Override
	public void updateEntity(Entity entity, Scene scene, float time) {
		CRender render = (CRender) entity.components().get(CRender.NAME);
		CAnimation animation = (CAnimation) entity.components().get(CAnimation.NAME);
		Animation current = animation.getCurrentAnimation();
		if (current == null)
			return;

		// if on first frame
		if (current.isFirstFrame()) {
			if (current.isFinished()) {
				Logger.instance().warn("No frames in animation on Entity: " + entity);
				return;
			}
			Material2D material = current.getFrames().get(0).getMaterial();
			render.setMaterial(material);
			current.setFirstFrame(false);
		}

		animation.setTimeElapsed(animation.getTimeElapsed() + time);
		float elapsed = animation.getTimeElapsed();

		Frame frame = current.currentFrame();

		while (elapsed >= frame.getTime()) {
			int nextFrame = current.getCurrentFrame() + 1;
			if (current.isFinished()) {
				if (current.isRepeating()) {
					nextFrame = 0;
				} else {
					animation.setTimeElapsed(0f);
					return;
				}
			}

			current.setCurrentFrame(nextFrame);
			Frame next = current.currentFrame();
			Material2D material = next.getMaterial();
			render.setMaterial(material);
			elapsed -= frame.getTime();
			animation.setTimeElapsed(elapsed);
			frame = next;
		}
	}

	@Override
	public void postUpdate(Scene scene) {
	}

	@Override
	public EntityFilter getUpdateFilter() {
		return s_filter;
	}

	@Override
	public EntityFilter getEntityEventFilter() {
		return s_filter;
	}
}
