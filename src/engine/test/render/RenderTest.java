package engine.test.render;

import commons.Resource;
import commons.ResourceLocator.ClasspathResourceLocator;
import commons.Transform2f;
import commons.matrix.Vector2f;

import engine.core.Entity;
import engine.core.EntityBuilder;
import engine.core.Game;
import engine.core.Scene;
import engine.core.script.XPython;
import engine.imp.render.CCamera;
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
 * Tests the rendering. Uses art from the public domain Glitch game.
 */
public class RenderTest {
	private MaterialFactory m_factory;
	private Resource m_codeResource;

	private Resource m_rIcicle;
	private Material m_icicle;
	private Resource m_rLake;
	private Material m_lake;
	private Resource m_rPlatform1;
	private Material m_platform1;
	private Resource m_rPlatform4;
	private Material m_platform4;

	public RenderTest() {
		m_codeResource = new Resource(new ClasspathResourceLocator(), "engine/test/render/Script.py");
		m_rIcicle = new Resource(new ClasspathResourceLocator(), "engine/test/render/cave_icicle_1_0.png");
		m_rLake = new Resource(new ClasspathResourceLocator(), "engine/test/render/cave_lake_1_0.png");
		m_rPlatform1 = new Resource(new ClasspathResourceLocator(), "engine/test/render/cave_platform_1_0.png");
		m_rPlatform4 = new Resource(new ClasspathResourceLocator(), "engine/test/render/cave_platform_4_0.png");
	}

	public void start() {
		Game game = new Game();
		RenderingSystem rendering = new RenderingSystem(2f, 2f);
		LightingSystem lighting = new LightingSystem(rendering);
		game.addSystem(rendering);
		game.addSystem(lighting);

		m_factory = new MaterialFactory(rendering);
		m_icicle = m_factory.createLighted(m_rIcicle);
		m_lake = m_factory.createLighted(m_rLake);
		m_platform1 = m_factory.createLighted(m_rPlatform1);
		m_platform4 = m_factory.createLighted(m_rPlatform4);

		Scene scene = new Scene(game);
		game.scenes().addScene(scene, "main");

		EntityBuilder lightBuilder = new EntityBuilder();
		lightBuilder.addComponentBuilder(new CLight(LightFactory.createDiffusePoint(new Vector3f(0f, 0f, 1f),
				new Vector3f(0.5f, 0.5f, 0.5f), new Color(0.7f, 0.7f, 1f))));
		scene.createEntity("light", scene, lightBuilder);

		EntityBuilder bIcicle = new EntityBuilder();
		bIcicle.addComponentBuilder(new CRender(m_icicle, 1, 1f));
		EntityBuilder bLake = new EntityBuilder();
		bLake.addComponentBuilder(new CRender(m_lake, 1, 1f));
		EntityBuilder bPlatform1 = new EntityBuilder();
		bPlatform1.addComponentBuilder(new CRender(m_platform1, 0, 2f));
		EntityBuilder bPlatform4 = new EntityBuilder();
		bPlatform4.addComponentBuilder(new CRender(m_platform4, 0, 2f));

		Entity platform1 = scene.createEntity("platform1", scene, bPlatform1);
		platform1.getCTransform().setTransform(new Transform2f(new Vector2f(-0.3f, 0f), 0f, new Vector2f(2f, 2f)));
		Entity platform2 = scene.createEntity("platform2", scene, bPlatform4);
		platform2.getCTransform().setTransform(new Transform2f(new Vector2f(1f, 0f), 0f, new Vector2f(2f, 2f)));

		Entity icicle = scene.createEntity("icicle1", scene, bIcicle);
		icicle.getCTransform().setTransform(
				new Transform2f(new Vector2f(0.5f, 0.6f), (float) Math.PI, new Vector2f(0.5f, 1f)));
		Entity lake = scene.createEntity("lake", scene, bLake);
		lake.getCTransform().translate(0f, -0.8f);

		EntityBuilder cameraBuilder = new EntityBuilder();
		cameraBuilder.addComponentBuilder(new CCamera(0.5f, true));
		Entity camera = scene.createEntity("camera", scene, cameraBuilder);
		camera.scripts().add(new XPython(m_codeResource));
		/*EntityBuilder backgroundBuilder = new EntityBuilder();
		backgroundBuilder.addComponentBuilder(new CRender(m_icicle, 0, 2f));
		backgroundBuilder.setTransform(new Transform2f(new Vector2f(0f, 0f), 0f, new Vector2f(2f, 2f)));
		scene.createEntity("background", scene, backgroundBuilder);

		EntityBuilder parentBuilder = new EntityBuilder();
		parentBuilder.addComponentBuilder(new CRender(m_icicle, 1, 1f));
		Entity parent = scene.createEntity("test", scene, parentBuilder);
		parent.scripts().add(new XPython(m_codeResource));

		EntityBuilder childBuilder = new EntityBuilder();
		childBuilder.addComponentBuilder(new CRender(m_icicle, 1, 1f));
		Entity child = scene.createEntity("child", parent, childBuilder);
		child.getCTransform().translate(0.5f, 0.5f);

		EntityBuilder cameraBuilder = new EntityBuilder();
		cameraBuilder.addComponentBuilder(new CCamera(0.5f, true));
		scene.createEntity("camera", child, cameraBuilder);*/

		game.start();

		while (true) {
			game.update(0.166666666f);
		}
	}

	public static void main(String[] args) {
		new RenderTest().start();
	}
}
