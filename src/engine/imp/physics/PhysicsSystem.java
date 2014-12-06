package engine.imp.physics;

import java.util.HashMap;
import java.util.Map;

import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import commons.Transform2f;
import commons.matrix.Matrix;
import commons.matrix.MatrixFactory;
import commons.matrix.Vector2f;
import commons.matrix.Vector3f;

import engine.core.Entity;
import engine.core.EntityListener;
import engine.core.EntitySystem;
import engine.core.Scene;
import engine.core.SimpleEntityFilter;
import engine.core.TreeNode;
import engine.imp.render.RenderingSystem;

/**
 * Manages the physics of a Game.
 */
public class PhysicsSystem implements EntitySystem {
	public static final int VELOCITY_ITERATIONS = 10;
	public static final int POSITION_ITERATIONS = 8;

	private static final SimpleEntityFilter s_updateFilter = new SimpleEntityFilter(new String[] { CPhysics.NAME },
			new String[0], new String[0], true);
	private static final SimpleEntityFilter s_eventFilter = new SimpleEntityFilter(new String[] { CPhysics.NAME },
			new String[0], new String[0], false);
	private World m_world;

	private Map<CPhysics, Body> m_bodies = new HashMap<CPhysics, Body>();

	/**
	 * Initializes a PhysicsSystem with a certain gravity.
	 * 
	 * @param gravity
	 */
	public PhysicsSystem(Vector2f gravity) {
		m_world = new World(new Vec2(gravity.getX(), gravity.getY()));
	}

	/**
	 * Gets the Body for a CPhysics.
	 * 
	 * @param physics
	 * @return
	 */
	protected Body getBody(CPhysics physics) {
		return m_bodies.get(physics);
	}

	@Override
	public void entityAdded(Entity entity, TreeNode parent, Scene scene) {
		CPhysics entityPhysics = (CPhysics) entity.components().get(CPhysics.NAME);
		Transform2f worldTransform = scene.getWorldTransform(entity);

		PhysicsData physics = entityPhysics.getPhysicsData();
		BodyDef bodyDef = new BodyDef();
		bodyDef.bullet = physics.isBullet();
		bodyDef.allowSleep = physics.allowSleep();
		bodyDef.fixedRotation = physics.isFixedRotation();
		bodyDef.linearDamping = physics.getLinearDamping();
		bodyDef.angularDamping = physics.getAngularDamping();
		bodyDef.gravityScale = physics.getGravityScale();
		bodyDef.type = physics.getType();

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = physics.getFriction();
		fixtureDef.restitution = physics.getRestitution();
		fixtureDef.density = 1f; // a nonsense value that gets overriden
		if (physics.getShape() == null) {
			Matrix transMatrix = MatrixFactory.identity(3);
			transMatrix = transMatrix.multiply(MatrixFactory.affineScale(worldTransform.getScale()));
			Vector3f p1 = transMatrix.multiply(
					new Vector3f(-RenderingSystem.HALF_DRAW_WIDTH, -RenderingSystem.HALF_DRAW_WIDTH, 1)).toVector3f();
			Vector3f p2 = transMatrix.multiply(
					new Vector3f(-RenderingSystem.HALF_DRAW_WIDTH, RenderingSystem.HALF_DRAW_WIDTH, 1)).toVector3f();
			Vector3f p3 = transMatrix.multiply(
					new Vector3f(RenderingSystem.HALF_DRAW_WIDTH, RenderingSystem.HALF_DRAW_WIDTH, 1)).toVector3f();
			Vector3f p4 = transMatrix.multiply(
					new Vector3f(RenderingSystem.HALF_DRAW_WIDTH, -RenderingSystem.HALF_DRAW_WIDTH, 1)).toVector3f();
			System.out.println(p1 + ", " + p2 + ", " + p3 + ", " + p4);
			Vec2[] vertices = new Vec2[4];
			vertices[0] = new Vec2(p1.getX(), p1.getY());
			vertices[1] = new Vec2(p2.getX(), p2.getY());
			vertices[2] = new Vec2(p3.getX(), p3.getY());
			vertices[3] = new Vec2(p4.getX(), p4.getY());
			PolygonShape shape = new PolygonShape();
			shape.set(vertices, 4);
			fixtureDef.shape = shape;
		} else {
			fixtureDef.shape = physics.getShape();
		}

		Body body = m_world.createBody(bodyDef);
		body.createFixture(fixtureDef);

		body.setTransform(new Vec2(worldTransform.getTranslation().getX(), worldTransform.getTranslation().getY()),
				worldTransform.getRotation());

		// set mass
		MassData data = new MassData();
		body.getMassData(data);
		data.mass = physics.getMass();
		body.setMassData(data);

		m_bodies.put(entityPhysics, body);
		entityPhysics.setPhysicsSystem(this);

		entity.addListener(new PhysicsListener());
	}

	@Override
	public void entityRemoved(Entity entity, TreeNode parent, Scene scene) {
		CPhysics entityPhysics = (CPhysics) entity.components().get(CPhysics.NAME);
		m_bodies.remove(entityPhysics);
	}

	@Override
	public void entityMoved(Entity entity, TreeNode oldParent, TreeNode newParent, Scene scene) {

	}

	@Override
	public void update(float time, Scene scene) {
		m_world.step(time * 0.01f, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	}

	@Override
	public void updateEntity(Entity entity, Scene scene) {
		CPhysics entityPhysics = (CPhysics) entity.components().get(CPhysics.NAME);
		Body body = m_bodies.get(entityPhysics);
		Vec2 pos = body.getPosition();
		entity.getCTransform().quietSetTransform(
				new Transform2f(new Vector2f(pos.x, pos.y), body.getAngle(), entity.getCTransform().getTransform()
						.getScale()));
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
		public void childAdded(Entity entity, Entity child) {
		}

		@Override
		public void childRemoved(Entity entity, Entity child) {
		}

		@Override
		public void parentChanged(Entity entity, TreeNode oldParent, TreeNode newParent, Transform2f oldTransform,
				Transform2f newTransform) {
		}

		@Override
		public void transformSet(Entity entity, Transform2f oldTransform, Transform2f newTransform) {
			Body body = m_bodies.get(entity.components().get(CPhysics.NAME));
			body.setTransform(new Vec2(newTransform.getTranslation().getX(), newTransform.getTranslation().getY()),
					newTransform.getRotation());

			// TODO: scaling
		}
	}
}
