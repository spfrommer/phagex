package test.render;

import commons.Resource;
import commons.ResourceLocator;
import commons.Transform2f;
import commons.matrix.Vector2f;
import commons.matrix.Vector3f;

import engine.core.Entity;
import engine.core.EntityBuilder;
import engine.core.Game;
import engine.core.Scene;
import engine.core.asset.AssetManager;
import engine.core.script.XPython;
import engine.imp.render.Animation;
import engine.imp.render.AnimationSystem;
import engine.imp.render.CAnimation;
import engine.imp.render.CCamera;
import engine.imp.render.CLight;
import engine.imp.render.CRender;
import engine.imp.render.LightFactory;
import engine.imp.render.LightingSystem;
import engine.imp.render.Material2D;
import engine.imp.render.RenderingSystem;
import glcommon.Color;

/**
 * Tests the rendering. Uses art from the public domain Glitch game.
 */
public class RenderTest {
	public RenderTest() {

	}

	public void start() {
		Game game = new Game();
		AnimationSystem animation = new AnimationSystem();
		RenderingSystem rendering = new RenderingSystem(2f, 2f);
		LightingSystem lighting = new LightingSystem(rendering);
		game.addSystem(animation);
		game.addSystem(rendering);
		game.addSystem(lighting);

		loadAssets(rendering);

		Scene scene = new Scene(game);
		game.scenes().addScene(scene, "main");

		EntityBuilder lightBuilder = new EntityBuilder();
		lightBuilder.addComponentBuilder(new CLight(LightFactory.createDiffusePoint(new Vector3f(0f, 0f, 1f),
				new Vector3f(0.5f, 0.5f, 0.5f), new Color(0.7f, 0.7f, 1f))));
		scene.createEntity("light", scene, lightBuilder);

		makeBackground(scene);
		makeForeground(scene);
		makeTreads(scene);

		EntityBuilder cameraBuilder = new EntityBuilder();
		cameraBuilder.addComponentBuilder(new CCamera(0.5f, true));
		scene.createEntity("camera", scene, cameraBuilder);

		game.start();

		float lastTime = 16f;
		while (true) {
			long startTime = System.nanoTime();
			game.update(lastTime);
			long endTime = System.nanoTime();
			lastTime = (endTime - startTime) / 1000000;
		}
	}

	private void makeBackground(Scene scene) {
		AssetManager manager = AssetManager.instance();

		EntityBuilder bBackground = new EntityBuilder();

		EntityBuilder bPlatform1 = new EntityBuilder();
		bPlatform1.addComponentBuilder(new CRender(manager.get("platform1", Material2D.class), 0, 2f));
		bPlatform1.setTransform(new Transform2f(new Vector2f(-0.3f, 0f), 0f, new Vector2f(2f, 2f)));

		EntityBuilder bPlatform2 = new EntityBuilder();
		bPlatform2.addComponentBuilder(new CRender(manager.get("platform2", Material2D.class), 0, 2f));
		bPlatform2.setTransform(new Transform2f(new Vector2f(1f, 0f), 0f, new Vector2f(2f, 2f)));

		bBackground.addChildBuilder("platform1", bPlatform1);
		bBackground.addChildBuilder("platform2", bPlatform2);

		Entity background = scene.createEntity("background", scene, bBackground);
		background.transform().translate(1f, 0f);
	}

	private void makeForeground(Scene scene) {
		AssetManager manager = AssetManager.instance();

		EntityBuilder bIcicle = new EntityBuilder();
		bIcicle.addComponentBuilder(new CRender(manager.get("icicle", Material2D.class), 2, 1f));
		bIcicle.addScript(manager.get("script", XPython.class));

		EntityBuilder bLake = new EntityBuilder();
		CRender clake = new CRender(manager.get("lake", Material2D.class), 2, 1f);
		clake.setRepeatX(2);
		clake.setRepeatY(2);
		bLake.addComponentBuilder(clake);

		Entity icicle = scene.createEntity("icicle1", scene, bIcicle);
		icicle.transform().setTransform(
				new Transform2f(new Vector2f(0.5f, 0.6f), (float) Math.PI, new Vector2f(0.5f, 1f)));

		Entity lake = scene.createEntity("lake", scene, bLake);
		lake.transform().translate(0f, -0.8f);
	}

	private void makeTreads(Scene scene) {
		AssetManager manager = AssetManager.instance();
		Animation animation = manager.get("treads", Animation.class);

		EntityBuilder bTreads = new EntityBuilder();
		CAnimation animator = new CAnimation();
		animator.addAnimation("glow", animation);
		animator.playAnimation("glow");
		bTreads.addComponentBuilder(animator);
		bTreads.addComponentBuilder(new CRender(manager.get("frame1", Material2D.class), 1, 1.5f));
		scene.createEntity("treads", scene, bTreads);
	}

	private void loadAssets(RenderingSystem system) {
		AssetManager manager = AssetManager.init(system.getRenderer().getGL());

		manager.loadFromFile(new Resource(new ResourceLocator.ClasspathResourceLocator(), "test/render/assets.ast"));
	}

	public static void main(String[] args) {
		new RenderTest().start();
	}
}
