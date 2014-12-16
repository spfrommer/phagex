package test.physics;

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
import engine.core.asset.AssetManager;
import engine.core.script.XPython;
import engine.imp.physics.dyn4j.BodySystem;
import engine.imp.physics.dyn4j.CBody;
import engine.imp.physics.dyn4j.CJoint;
import engine.imp.physics.dyn4j.JointSystem;
import engine.imp.physics.dyn4j.PhysicsFactory;
import engine.imp.render.CLight;
import engine.imp.render.CRender;
import engine.imp.render.LightFactory;
import engine.imp.render.LightingSystem;
import engine.imp.render.Material2D;
import engine.imp.render.RenderingSystem;
import glcommon.Color;

/**
 * Tests the Dyn4j physics.
 */
public class Dyn4jPhysicsTest {
	public Dyn4jPhysicsTest() {
	}

	public void start() {
		Game game = new Game();
		RenderingSystem rendering = new RenderingSystem(20f, 20f);
		LightingSystem lighting = new LightingSystem(rendering);
		BodySystem bodies = new BodySystem(new Vector2f(0f, -10f));
		JointSystem joints = new JointSystem(bodies);
		game.addSystem(rendering);
		game.addSystem(lighting);
		game.addSystem(bodies);
		game.addSystem(joints);

		loadAssets(rendering);

		Scene scene = new Scene(game);
		game.scenes().addScene(scene, "main");

		EntityBuilder lightBuilder = new EntityBuilder();
		lightBuilder.addComponentBuilder(new CLight(LightFactory.createDiffusePoint(new Vector3f(0f, 0f, 1f),
				new Vector3f(0.5f, 0.5f, 0.5f), new Color(1f, 1f, 1f))));
		scene.createEntity("light", scene, lightBuilder);

		makeScene(scene);

		game.start();
		float lastTime = 16f;
		while (true) {
			long startTime = System.nanoTime();
			game.update(lastTime);
			long endTime = System.nanoTime();
			lastTime = (endTime - startTime) / 1000000;
		}
	}

	private void loadAssets(RenderingSystem system) {
		AssetManager manager = AssetManager.init(system.getRenderer().getGL());

		ClasspathResourceLocator locator = new ClasspathResourceLocator();
		Resource brickScript = new Resource(locator, "test/physics/BrickScript.py");
		Resource lightScript = new Resource(locator, "test/physics/LightScript.py");
		Resource testTube = new Resource(locator, "test/physics/testtube.jpg");

		manager.load("brick_script", brickScript, XPython.class);
		manager.load("light_script", lightScript, XPython.class);
		manager.load("testtube", testTube, Material2D.class);
	}

	private void makeScene(Scene scene) {
		AssetManager manager = AssetManager.instance();

		EntityBuilder brickBuilder = new EntityBuilder();
		brickBuilder.addComponentBuilder(new CRender(manager.get("testtube", Material2D.class)));
		brickBuilder.addComponentBuilder(new CBrickBuilder());
		Entity brick1 = scene.createEntity("brick1", scene, brickBuilder);
		brick1.getCTransform().setTransform(new Transform2f(new Vector2f(0f, 2f), 0f, new Vector2f(1f, 1f)));
		Entity brick2 = scene.createEntity("brick2", scene, brickBuilder);
		brick1.fields().set(CBody.DENSITY, 100f);
		brick2.getCTransform().setTransform(new Transform2f(new Vector2f(4f, 2f), 0f, new Vector2f(1f, 1f)));

		EntityBuilder jointBuilder = new EntityBuilder();
		jointBuilder.addComponentBuilder(new CJointBuilder(PhysicsFactory.createRevolute(brick1, brick2, new Vector2f(
				0f, 2f))));
		scene.createEntity("joint", scene, jointBuilder);

		EntityBuilder groundBuilder = new EntityBuilder();
		groundBuilder.addComponentBuilder(new CRender(manager.get("testtube", Material2D.class)));
		groundBuilder.addComponentBuilder(new CGroundBuilder());
		groundBuilder.setTransform(new Transform2f(new Vector2f(0f, -2f), 0f, new Vector2f(2f, 1f)));
		Entity ground = scene.createEntity("ground", scene, groundBuilder);
		ground.getCTransform().setTransform(new Transform2f(new Vector2f(0f, -1f), 0f, new Vector2f(2f, 1f)));
	}

	private class CBrickBuilder implements ComponentBuilder<CBody> {
		public CBrickBuilder() {
		}

		@Override
		public CBody build() {
			CBody physics = new CBody();
			physics.setShape(new Rectangle(1, 1));
			physics.setGravityScale(1);
			physics.setMassType(Type.NORMAL);
			physics.setDensity(5);
			physics.setCollisionFriction(10);
			return physics;
		}

		@Override
		public String getName() {
			return CBody.NAME;
		}
	}

	private class CJointBuilder implements ComponentBuilder<CJoint> {
		private Joint m_joint;

		public CJointBuilder(Joint joint) {
			m_joint = joint;
		}

		@Override
		public CJoint build() {
			return new CJoint(m_joint);
		}

		@Override
		public String getName() {
			return CJoint.NAME;
		}
	}

	private class CGroundBuilder implements ComponentBuilder<CBody> {
		@Override
		public CBody build() {
			CBody physics = new CBody();
			physics.setShape(new Rectangle(2, 1));
			physics.setGravityScale(1);
			physics.setMassType(Type.INFINITE);
			return physics;
		}

		@Override
		public String getName() {
			return CBody.NAME;
		}
	}

	public static void main(String[] args) {
		new Dyn4jPhysicsTest().start();
	}
}
