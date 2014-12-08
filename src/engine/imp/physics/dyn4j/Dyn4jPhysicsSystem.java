package engine.imp.physics.dyn4j;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import commons.Transform2f;
import commons.matrix.Vector2f;

import engine.core.Entity;
import engine.core.EntityListener;
import engine.core.EntitySystem;
import engine.core.Scene;
import engine.core.SimpleEntityFilter;
import engine.core.TreeNode;
import engine.imp.physics.PhysicsUtils;

/**
 * Manages the physics of a Game.
 */
public class Dyn4jPhysicsSystem implements EntitySystem {
	public static final int VELOCITY_ITERATIONS = 10;
	public static final int POSITION_ITERATIONS = 8;

	private static final SimpleEntityFilter s_updateFilter = new SimpleEntityFilter(
			new String[] { CDyn4jPhysics.NAME }, new String[0], new String[0], true);
	private static final SimpleEntityFilter s_eventFilter = new SimpleEntityFilter(new String[] { CDyn4jPhysics.NAME },
			new String[0], new String[0], false);
	private World m_world;

	/**
	 * Initializes a PhysicsSystem with a certain gravity.
	 * 
	 * @param gravity
	 */
	public Dyn4jPhysicsSystem(Vector2f gravity) {
		m_world = new World();
		m_world.setGravity(new Vector2(gravity.getX(), gravity.getY()));
	}

	@Override
	public void entityAdded(Entity entity, TreeNode parent, Scene scene) {
		CDyn4jPhysics entityPhysics = (CDyn4jPhysics) entity.components().get(CDyn4jPhysics.NAME);
		Body body = entityPhysics.getBody();

		Transform2f worldTransform = scene.getWorldTransform(entity);
		Transform newTrans = new Transform();
		newTrans.setTranslation(PhysicsUtils.toDyn4j(worldTransform.getTranslation()));
		newTrans.setRotation(worldTransform.getRotation());

		body.setTransform(newTrans);

		entity.addListener(new PhysicsListener());
		m_world.addBody(body);
	}

	@Override
	public void entityRemoved(Entity entity, TreeNode parent, Scene scene) {
		CDyn4jPhysics entityPhysics = (CDyn4jPhysics) entity.components().get(CDyn4jPhysics.NAME);
		m_world.removeBody(entityPhysics.getBody());
	}

	@Override
	public void entityMoved(Entity entity, TreeNode oldParent, TreeNode newParent, Scene scene) {

	}

	@Override
	public void update(float time, Scene scene) {
		m_world.update(time / 1000);
	}

	@Override
	public void updateEntity(Entity entity, Scene scene) {
		CDyn4jPhysics entityPhysics = (CDyn4jPhysics) entity.components().get(CDyn4jPhysics.NAME);

		Body body = entityPhysics.getBody();
		// TreeNode parent = entity.tree().getParent();

		Vector2 trans = body.getTransform().getTranslation();
		// Transform2f parentTransform = scene.getWorldTransform(parent);
		// TODO: transforms relative to parent
		entity.getCTransform().quietSetTransform(
				new Transform2f(PhysicsUtils.fromDyn4j(trans), (float) body.getTransform().getRotation(), entity
						.getCTransform().getTransform().getScale()));
	}

	@Override
	public void postUpdate(Scene scene) {

	}

	@Override
	public SimpleEntityFilter getUpdateFilter() {
		return s_updateFilter;
	}

	@Override
	public SimpleEntityFilter getEntityEventFilter() {
		return s_eventFilter;
	}

	private class PhysicsListener implements EntityListener {
		@Override
		public void childAdded(Entity entity, Entity child, Scene scene) {
		}

		@Override
		public void childRemoved(Entity entity, Entity child, Scene scene) {
		}

		@Override
		public void parentChanged(Entity entity, TreeNode oldParent, TreeNode newParent, Transform2f oldLocalTransform,
				Transform2f newLocalTransform, Transform2f oldWorldTransform, Transform2f newWorldTransform, Scene scene) {
		}

		@Override
		public void transformSet(Entity entity, Transform2f oldTransform, Transform2f newTransform, Scene scene) {
			Body body = ((CDyn4jPhysics) entity.components().get(CDyn4jPhysics.NAME)).getBody();
			Transform2f worldTrans = scene.getWorldTransform(entity);
			Transform newTrans = new Transform();
			newTrans.setTranslation(worldTrans.getTranslation().getX(), worldTrans.getTranslation().getY());
			newTrans.setRotation(worldTrans.getRotation());
			body.setTransform(newTrans);
			// TODO: scaling
		}
	}
}
