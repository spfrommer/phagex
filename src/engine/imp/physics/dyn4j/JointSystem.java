package engine.imp.physics.dyn4j;

import java.util.List;

import org.dyn4j.dynamics.World;

import engine.core.Entity;
import engine.core.EntityFilter;
import engine.core.EntitySystem;
import engine.core.Scene;
import engine.core.SimpleEntityFilter;
import engine.core.TreeNode;
import engine.core.script.XScript;

/**
 * Manages the Joints in a game.
 */
public class JointSystem implements EntitySystem {
	private static final SimpleEntityFilter s_updateFilter = new SimpleEntityFilter(new String[] { CJoint.NAME }, new String[0],
			true);
	private static final SimpleEntityFilter s_eventFilter = new SimpleEntityFilter(new String[] { CJoint.NAME }, new String[0],
			false);
	private BodySystem m_bodySystem;

	public JointSystem(BodySystem bodySystem) {
		m_bodySystem = bodySystem;
	}

	@Override
	public void sceneChanged(Scene oldScene, Scene newScene) {
		m_bodySystem.getWorld().removeAllJoints();
		List<Entity> newAll = newScene.getEntitiesByFilter(s_eventFilter);
		for (Entity e : newAll) {
			CJoint entityPhysics = (CJoint) e.components().get(CJoint.NAME);
			World world = m_bodySystem.getWorld();
			world.addJoint(entityPhysics.getJoint());
		}
	}

	@Override
	public void entityAdded(Entity entity, TreeNode parent, Scene scene) {
		if (scene.isCurrent()) {
			CJoint entityPhysics = (CJoint) entity.components().get(CJoint.NAME);
			World world = m_bodySystem.getWorld();
			world.addJoint(entityPhysics.getJoint());
		}
	}

	@Override
	public void entityRemoved(Entity entity, TreeNode parent, Scene scene) {
		if (scene.isCurrent()) {
			CJoint entityPhysics = (CJoint) entity.components().get(CJoint.NAME);
			World world = m_bodySystem.getWorld();
			world.removeJoint(entityPhysics.getJoint());
		}
	}

	@Override
	public void entityMoved(Entity entity, TreeNode oldParent, TreeNode newParent, Scene scene) {
	}

	@Override
	public void entityEnabled(Entity entity, TreeNode parent, Scene scene) {
		entityAdded(entity, parent, scene);
	}

	@Override
	public void entityDisabled(Entity entity, TreeNode parent, Scene scene) {
		entityRemoved(entity, parent, scene);
	}

	@Override
	public void update(Scene scene, float time) {
	}

	@Override
	public void updateEntity(Entity entity, Scene scene, float time) {
	}

	@Override
	public void postUpdate(Scene scene) {
	}

	@Override
	public EntityFilter getUpdateFilter() {
		return s_updateFilter;
	}

	@Override
	public EntityFilter getEntityEventFilter() {
		return s_eventFilter;
	}

	@Override
	public void scriptAdded(Entity entity, XScript script, Scene scene) {
	}
}
