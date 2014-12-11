package engine.test.render;

import commons.Resource;
import commons.ResourceLocator.ClasspathResourceLocator;

import engine.core.Entity;
import engine.core.EntityBuilder;
import engine.core.Game;
import engine.core.Scene;
import engine.core.script.XPython;
import engine.imp.render.CLight;
import engine.imp.render.CRender;
import engine.imp.render.LightFactory;
import engine.imp.render.LightingSystem;
import engine.imp.render.MaterialFactory;
import engine.imp.render.RenderingSystem;
import glcommon.Color;
import glcommon.vector.Vector3f;
import glextra.material.Material;

/**
 * Tests the rendering.
 */
public class RenderTest {
	private MaterialFactory m_factory;
	private Resource m_codeResource;
	private Resource m_materialResource;
	private Material m_material;

	public RenderTest() {
		m_codeResource = new Resource(new ClasspathResourceLocator(), "engine/test/render/Script.py");
		m_materialResource = new Resource(new ClasspathResourceLocator(), "engine/test/render/testtube.jpg");
	}

	public void start() {
		Game game = new Game();
		RenderingSystem rendering = new RenderingSystem(2f, 2f);
		LightingSystem lighting = new LightingSystem(rendering);
		game.addSystem(rendering);
		game.addSystem(lighting);

		m_factory = new MaterialFactory(rendering);
		m_material = m_factory.createLighted(m_materialResource);

		Scene scene = new Scene(game);
		game.scenes().addScene(scene, "main");

		EntityBuilder lightBuilder = new EntityBuilder();
		lightBuilder.addComponentBuilder(new CLight(LightFactory.createDiffusePoint(new Vector3f(0f, 0f, 1f),
				new Vector3f(1f, 0.5f, 0.5f), new Color(1f, 0f, 0f))));
		scene.createEntity("light", scene, lightBuilder);

		EntityBuilder parentBuilder = new EntityBuilder();
		parentBuilder.addComponentBuilder(new CRender(m_material, 1f));
		Entity parent = scene.createEntity("test", scene, parentBuilder);
		parent.scripts().add(new XPython(m_codeResource));

		EntityBuilder childBuilder = new EntityBuilder();
		childBuilder.addComponentBuilder(new CRender(m_material, 1f));
		Entity child = scene.createEntity("child", parent, childBuilder);
		child.getCTransform().translate(0.5f, 0.5f);

		game.start();
		while (true) {
			game.update(0.166666666f);
		}
	}

	public static void main(String[] args) {
		new RenderTest().start();
	}
}
