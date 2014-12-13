package engine.test.physics;

import org.dyn4j.dynamics.joint.Joint;
import org.dyn4j.geometry.Mass.Type;
import org.dyn4j.geometry.Rectangle;

import commons.Resource;
import commons.ResourceLocator.ClasspathResourceLocator;
import commons.Transform2f;
import commons.matrix.Vector2f;
import commons.matrix.Vector3f;

import engine.core.ComponentBuilder;
import engine.core.Entity;
import engine.core.EntityBuilder;
import engine.core.Game;
import engine.core.Scene;
import engine.imp.physics.dyn4j.CDyn4jBody;
import engine.imp.physics.dyn4j.CDyn4jJoint;
import engine.imp.physics.dyn4j.Dyn4jBodySystem;
import engine.imp.physics.dyn4j.Dyn4jJointSystem;
import engine.imp.physics.dyn4j.PhysicsFactory;
import engine.imp.render.CLight;
import engine.imp.render.CRender;
import engine.imp.render.LightFactory;
import engine.imp.render.LightingSystem;
import engine.imp.render.MaterialFactory;
import engine.imp.render.RenderingSystem;
import glcommon.Color;
import glextra.material.Material;
import gltools.input.Keyboard;

/**
 * Tests the Dyn4j physics.
 */
public class Dyn4jPhysicsTest {
	private MaterialFactory m_factory;
	// private Resource m_brickScript;
	private Resource m_materialResource;
	private Material m_material;

	public Dyn4jPhysicsTest() {
		// m_brickScript = new Resource(new ClasspathResourceLocator(), "engine/test/physics/Script.py");
		m_materialResource = new Resource(new ClasspathResourceLocator(), "engine/test/physics/testtube.jpg");
	}

	public void start() {
		Game game = new Game();
		RenderingSystem rendering = new RenderingSystem(20f, 20f);
		LightingSystem lighting = new LightingSystem(rendering);
		Dyn4jBodySystem bodies = new Dyn4jBodySystem(new Vector2f(0f, -10f));
		Dyn4jJointSystem joints = new Dyn4jJointSystem(bodies);
		game.addSystem(rendering);
		game.addSystem(lighting);
		game.addSystem(bodies);
		game.addSystem(joints);

		m_factory = new MaterialFactory(rendering);
		m_material = m_factory.createLighted(m_materialResource);

		Scene scene = new Scene(game);
		game.scenes().addScene(scene, "main");

		EntityBuilder brickBuilder = new EntityBuilder();
		brickBuilder.addComponentBuilder(new CRender(m_material, 1f));
		brickBuilder.addComponentBuilder(new CBrickBuilder());
		Entity brick1 = scene.createEntity("brick1", scene, brickBuilder);
		brick1.getCTransform().setTransform(new Transform2f(new Vector2f(0f, 2f), 0f, new Vector2f(1f, 1f)));
		Entity brick2 = scene.createEntity("brick2", scene, brickBuilder);
		brick1.fields().set(CDyn4jBody.DENSITY, 100f);
		brick2.getCTransform().setTransform(new Transform2f(new Vector2f(4f, 2f), 0f, new Vector2f(1f, 1f)));

		EntityBuilder jointBuilder = new EntityBuilder();
		jointBuilder.addComponentBuilder(new CJointBuilder(PhysicsFactory.createRevolute(brick1, brick2, new Vector2f(
				0f, 2f))));
		scene.createEntity("joint", scene, jointBuilder);

		EntityBuilder lightBuilder = new EntityBuilder();
		lightBuilder.addComponentBuilder(new CLight(LightFactory.createDiffusePoint(new Vector3f(0f, 0f, 1f),
				new Vector3f(0.5f, 0.5f, 0.5f), new Color(1f, 1f, 1f))));
		scene.createEntity("light", brick1, lightBuilder);

		EntityBuilder groundBuilder = new EntityBuilder();
		groundBuilder.addComponentBuilder(new CRender(m_material, 1f));
		groundBuilder.addComponentBuilder(new CGroundBuilder());
		groundBuilder.setTransform(new Transform2f(new Vector2f(0f, -2f), 0f, new Vector2f(2f, 1f)));
		Entity ground = scene.createEntity("ground", scene, groundBuilder);
		ground.getCTransform().setTransform(new Transform2f(new Vector2f(0f, -1f), 0f, new Vector2f(2f, 1f)));

		game.start();
		float lastTime = 16f;
		while (true) {
			Keyboard keyboard = rendering.getKeyboard();
			if (keyboard.isKeyPressed(keyboard.getKey("UP"))) {
				CDyn4jBody physicsComp = (CDyn4jBody) brick1.components().get(CDyn4jBody.NAME);
				physicsComp.applyForce(new Vector2f(0f, 1f));
				physicsComp.applyTorque(1f);
			}
			long startTime = System.nanoTime();
			game.update(lastTime);
			long endTime = System.nanoTime();
			lastTime = (endTime - startTime) / 1000000;
		}
	}

	private class CBrickBuilder implements ComponentBuilder<CDyn4jBody> {
		public CBrickBuilder() {
		}

		@Override
		public CDyn4jBody build() {
			CDyn4jBody physics = new CDyn4jBody();
			physics.setShape(new Rectangle(1, 1));
			physics.setGravityScale(1);
			physics.setMassType(Type.NORMAL);
			physics.setDensity(5);
			physics.setCollisionFriction(10);
			return physics;
		}

		@Override
		public String getName() {
			return CDyn4jBody.NAME;
		}
	}

	private class CJointBuilder implements ComponentBuilder<CDyn4jJoint> {
		private Joint m_joint;

		public CJointBuilder(Joint joint) {
			m_joint = joint;
		}

		@Override
		public CDyn4jJoint build() {
			return new CDyn4jJoint(m_joint);
		}

		@Override
		public String getName() {
			return CDyn4jJoint.NAME;
		}
	}

	private class CGroundBuilder implements ComponentBuilder<CDyn4jBody> {
		@Override
		public CDyn4jBody build() {
			CDyn4jBody physics = new CDyn4jBody();
			physics.setShape(new Rectangle(2, 1));
			physics.setGravityScale(1);
			physics.setMassType(Type.INFINITE);
			return physics;
		}

		@Override
		public String getName() {
			return CDyn4jBody.NAME;
		}
	}

	public static void main(String[] args) {
		new Dyn4jPhysicsTest().start();
	}
}
