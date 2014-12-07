package engine.test.physics;

import org.jbox2d.dynamics.BodyType;

import commons.Logger;
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
import engine.imp.physics.CPhysics;
import engine.imp.physics.PhysicsData;
import engine.imp.physics.PhysicsSystem;
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

public class PhysicsTest {
	private MaterialFactory m_factory;
	private Resource m_brickScript;
	private Resource m_lightScript;
	private Resource m_materialResource;
	private Material m_material;

	public PhysicsTest() {
		m_brickScript = new Resource(new ClasspathResourceLocator(), "engine/test/physics/Script.py");
		m_lightScript = new Resource(new ClasspathResourceLocator(), "engine/test/physics/LightScript.py");
		m_materialResource = new Resource(new ClasspathResourceLocator(), "engine/test/physics/testtube.jpg");
	}

	public void start() {
		Game game = new Game();
		RenderingSystem rendering = new RenderingSystem(20f, 20f);
		LightingSystem lighting = new LightingSystem(rendering);
		PhysicsSystem physics = new PhysicsSystem(new Vector2f(0f, -0.04f));
		game.addSystem(rendering);
		game.addSystem(lighting);
		game.addSystem(physics);

		m_factory = new MaterialFactory(rendering);
		m_material = m_factory.createLighted(m_materialResource);

		Scene scene = new Scene(game);
		game.scenes().addScene(scene, "main");

		PhysicsData brickData = new PhysicsData();
		brickData.setRestitution(0f);
		brickData.setMass(100f);

		EntityBuilder brickBuilder = new EntityBuilder();
		brickBuilder.addComponentBuilder(new CRender(m_material, 1f));
		brickBuilder.addComponentBuilder(new CPhysicsBuilder());
		Entity brick = scene.createEntity("brick", scene, brickBuilder);
		brick.getCTransform().setTransform(
				new Transform2f(new Vector2f(0f, 2f), (float) Math.PI / 4, new Vector2f(1f, 1f)));
		brick.scripts().add(new XPython(m_brickScript));

		EntityBuilder lightBuilder = new EntityBuilder();
		lightBuilder.addComponentBuilder(new CLight(LightFactory.createDiffusePoint(new Vector3f(0f, 0f, 1f),
				new Vector3f(0.5f, 0.5f, 0.5f), new Color(0.5f, 0.5f, 0.5f))));
		Entity light = scene.createEntity("light", brick, lightBuilder);
		// light.scripts().add(new XPython(m_lightScript));

		PhysicsData groundData = new PhysicsData();
		groundData.setType(BodyType.STATIC);
		groundData.setRestitution(0f);

		EntityBuilder groundBuilder = new EntityBuilder();
		groundBuilder.addComponentBuilder(new CRender(m_material, 1f));
		groundBuilder.addComponentBuilder(new CPhysicsBuilder(groundData));
		groundBuilder.setTransform(new Transform2f(new Vector2f(0f, -2f), 0f, new Vector2f(2f, 1f)));
		scene.createEntity("ground", scene, groundBuilder);

		game.start();
		float lastTime = 16f;
		int lightCount = 0;
		float lastLightCreate = System.nanoTime();
		while (true) {
			Keyboard keyboard = rendering.getKeyboard();
			if (keyboard.isKeyPressed(keyboard.getKey('l')) && System.nanoTime() - lastLightCreate > 1000000000) {
				lastLightCreate = System.nanoTime();
				EntityBuilder lBuilder = new EntityBuilder();
				lBuilder.addComponentBuilder(new CLight(LightFactory.createDiffusePoint(new Vector3f(0f, 0f, 1f),
						new Vector3f(0.01f, 0.01f, 3f), new Color((float) Math.random(), (float) Math.random(),
								(float) Math.random()))));
				Entity l = scene.createEntity("light" + lightCount, brick, lBuilder);
				l.scripts().add(new XPython(m_lightScript));
				lightCount++;
				System.out.println("Created light number " + lightCount);
			}
			if (keyboard.isKeyPressed(keyboard.getKey("UP"))) {
				CPhysics physicsComp = (CPhysics) brick.components().get(CPhysics.NAME);
				physicsComp.applyForce(new Vector2f(0f, 1f));
			}
			if (keyboard.isKeyPressed(keyboard.getKey("DOWN"))) {
				CPhysics physicsComp = (CPhysics) brick.components().get(CPhysics.NAME);
				physicsComp.applyForce(new Vector2f(0f, -1f));
			}
			long startTime = System.nanoTime();
			game.update(16f);
			long endTime = System.nanoTime();
			lastTime = (endTime - startTime) / 1000000;
			Logger.instance().out(lastTime);
		}
	}

	private class CPhysicsBuilder implements ComponentBuilder<CPhysics> {
		private PhysicsData m_data;

		public CPhysicsBuilder() {
			m_data = new PhysicsData();
		}

		public CPhysicsBuilder(PhysicsData data) {
			m_data = data;
		}

		@Override
		public CPhysics build() {
			CPhysics physics = new CPhysics(m_data);
			return physics;
		}

		@Override
		public String getName() {
			return CPhysics.NAME;
		}
	}

	public static void main(String[] args) {
		new PhysicsTest().start();
	}
}
