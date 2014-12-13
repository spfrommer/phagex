package engine.imp.render;

import java.util.Set;

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
	private static final SimpleEntityFilter s_filter = new SimpleEntityFilter(new String[] { CRender.NAME,
			CAnimation.NAME }, new String[0], new String[0], false);

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
		if (current == null) {
			Set<String> names = animation.allNames();
			String selected = null;
			iterate: {
				for (String s : names) {
					selected = s;
					break iterate;
				}
			}
			animation.setCurrentAnimation(selected);
			current = animation.getCurrentAnimation();
		}

		float timePerFrame = current.getTimePerFrame();
		animation.setTimeElapsed(animation.getTimeElapsed() + time);
		float elapsed = animation.getTimeElapsed();
		while (elapsed >= timePerFrame) {
			if (current.isFinished()) {
				if (current.isRepeating()) {
					current.setCurrentFrame(0);
				} else {
					animation.setTimeElapsed(0f);
					return;
				}
			}
			int nextFrame = current.getCurrentFrame() + 1;
			current.setCurrentFrame(nextFrame);
			render.setMaterial(current.getFrames().get(nextFrame));
			elapsed -= timePerFrame;
			animation.setTimeElapsed(elapsed);
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
