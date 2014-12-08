package engine.test.physics;

import org.dyn4j.geometry.Mass.Type;
import org.dyn4j.geometry.Rectangle;

import commons.Resource;
import commons.ResourceLocator.ClasspathResourceLocator;
import commons.Transform2f;
import commons.matrix.Vector2f;

import engine.core.ComponentBuilder;
import engine.core.Entity;
import engine.core.EntityBuilder;
import engine.core.Game;
import engine.core.Scene;
import engine.core.script.XPython;
import engine.imp.physics.dyn4j.CDyn4jPhysics;
import engine.imp.physics.dyn4j.Dyn4jPhysicsSystem;
import engine.imp.render.CLight;
import engine.imp.render.CRender;
import engine.imp.render.LightFactory;
import engine.imp.render.LightingSystem;
import engine.imp.render.MaterialFactory;
import engine.imp.render.RenderingSystem;
import glcommon.Color;
import glcommon.vector.Vector3f;
import glextra.material.Material;
import gltools.input.Keyboard;

/**
 * Tests the Dyn4j physics.
 */
public class Dyn4jPhysicsTest {
	private MaterialFactory m_factory;
	private Resource m_brickScript;
	private Resource m_lightScript;
	private Resource m_materialResource;
	private Material m_material;

	public Dyn4jPhysicsTest() {
		m_brickScript = new Resource(new ClasspathResourceLocator(), "engine/test/physics/Script.py");
		m_lightScript = new Resource(new ClasspathResourceLocator(), "engine/test/physics/LightScript.py");
		m_materialResource = new Resource(new ClasspathResourceLocator(), "engine/test/physics/testtube.jpg");
	}

	public void start() {
		Game game = new Game();
		RenderingSystem rendering = new RenderingSystem(20f, 20f);
		LightingSystem lighting = new LightingSystem(rendering);
		Dyn4jPhysicsSystem physics = new Dyn4jPhysicsSystem(new Vector2f(0f, -10f));
		game.addSystem(rendering);
		game.addSystem(lighting);
		game.addSystem(physics);

		m_factory = new MaterialFactory(rendering);
		m_material = m_factory.createLighted(m_materialResource);

		Scene scene = new Scene(game);
		game.scenes().addScene(scene, "main");

		EntityBuilder brickBuilder = new EntityBuilder();
		brickBuilder.addComponentBuilder(new CRender(m_material, 1f));
		brickBuilder.addComponentBuilder(new CBrickBuilder());
		Entity brick = scene.createEntity("brick", scene, brickBuilder);
		brick.getCTransform().setTransform(new Transform2f(new Vector2f(0f, 2f), 0f, new Vector2f(1f, 1f)));
		brick.scripts().add(new XPython(m_brickScript));

		EntityBuilder lightBuilder = new EntityBuilder();
		lightBuilder.addComponentBuilder(new CLight(LightFactory.createDiffusePoint(new Vector3f(0f, 0f, 1f),
				new Vector3f(0.5f, 0.5f, 0.5f), new Color(1f, 1f, 1f))));
		scene.createEntity("light", brick, lightBuilder);

		EntityBuilder groundBuilder = new EntityBuilder();
		groundBuilder.addComponentBuilder(new CRender(m_material, 1f));
		groundBuilder.addComponentBuilder(new CGroundBuilder());
		groundBuilder.setTransform(new Transform2f(new Vector2f(0f, -2f), 0f, new Vector2f(2f, 1f)));
		scene.createEntity("ground", scene, groundBuilder);

		game.start();
		float lastTime = 16f;
		while (true) {
			Keyboard keyboard = rendering.getKeyboard();
			if (keyboard.isKeyPressed(keyboard.getKey("UP"))) {
				CDyn4jPhysics physicsComp = (CDyn4jPhysics) brick.components().get(CDyn4jPhysics.NAME);
				physicsComp.applyForce(new Vector2f(0f, 1f));
				physicsComp.applyTorque(1f);
			}
			long startTime = System.nanoTime();
			game.update(lastTime);
			long endTime = System.nanoTime();
			lastTime = (endTime - startTime) / 1000000;
		}
	}

	private class CBrickBuilder implements ComponentBuilder<CDyn4jPhysics> {
		public CBrickBuilder() {
		}

		@Override
		public CDyn4jPhysics build() {
			CDyn4jPhysics physics = new CDyn4jPhysics();
			physics.setShape(new Rectangle(1, 1));
			physics.setGravityScale(1);
			physics.setMassType(Type.NORMAL);
			physics.setDensity(5);
			physics.setCollisionFriction(10);
			return physics;
		}

		@Override
		public String getName() {
			return CDyn4jPhysics.NAME;
		}
	}

	private class CGroundBuilder implements ComponentBuilder<CDyn4jPhysics> {
		public CGroundBuilder() {
		}

		@Override
		public CDyn4jPhysics build() {
			CDyn4jPhysics physics = new CDyn4jPhysics();
			physics.setShape(new Rectangle(2, 1));
			physics.setGravityScale(1);
			physics.setMassType(Type.INFINITE);
			return physics;
		}

		@Override
		public String getName() {
			return CDyn4jPhysics.NAME;
		}
	}

	public static void main(String[] args) {
		new Dyn4jPhysicsTest().start();
	}
}
