package engine.test.physics;

import org.jbox2d.dynamics.BodyType;

import commons.Resource;
import commons.ResourceLocator.ClasspathResourceLocator;
import commons.Transform2f;
import commons.matrix.Vector2f;

import engine.core.CTransform;
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

public class PhysicsTest {
	private MaterialFactory m_factory;
	private Resource m_codeResource;
	private Resource m_materialResource;
	private Material m_material;

	public PhysicsTest() {
		m_codeResource = new Resource(new ClasspathResourceLocator(), "engine/test/physics/Script.py");
		m_materialResource = new Resource(new ClasspathResourceLocator(), "engine/test/physics/testtube.jpg");
	}

	public void start() {
		Game game = new Game();
		RenderingSystem rendering = new RenderingSystem(10f, 10f);
		LightingSystem lighting = new LightingSystem(rendering);
		PhysicsSystem physics = new PhysicsSystem(new Vector2f(0f, -0.04f));
		game.addSystem(rendering);
		game.addSystem(lighting);
		game.addSystem(physics);

		m_factory = new MaterialFactory(rendering);
		m_material = m_factory.createLighted(m_materialResource);

		Scene scene = new Scene(game);
		game.scenes().addScene(scene, "main");

		EntityBuilder lightBuilder = new EntityBuilder();
		lightBuilder.addComponentBuilder(new CLight(LightFactory.createDiffusePoint(new Vector3f(0f, 0f, 1f),
				new Vector3f(1f, 0.5f, 0.5f), new Color(1f, 1f, 1f))));
		scene.createEntity("light", scene, lightBuilder);

		PhysicsData brickData = new PhysicsData();
		brickData.setRestitution(0.5f);

		EntityBuilder brickBuilder = new EntityBuilder();
		brickBuilder.addComponentBuilder(new CRender(m_material, 1f));
		brickBuilder.addComponentBuilder(new CPhysicsBuilder());
		Entity brick = scene.createEntity("brick", scene, brickBuilder);
		brick.getCTransform().setTransform(new Transform2f(new Vector2f(-0.8f, 0f), 0.1f, new Vector2f(1f, 1f)));
		brick.scripts().add(new XPython(m_codeResource));

		PhysicsData groundData = new PhysicsData();
		groundData.setType(BodyType.STATIC);
		groundData.setRestitution(0.5f);

		EntityBuilder groundBuilder = new EntityBuilder();
		groundBuilder.addComponentBuilder(new CRender(m_material, 1f));
		groundBuilder.addComponentBuilder(new CPhysicsBuilder(groundData));
		Entity ground = scene.createEntity("ground", scene, groundBuilder);
		ground.getCTransform().setData(CTransform.TRANSLATION, new Vector2f(0f, -2f));

		game.start();
		while (true) {
			game.update(0.16f);
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
