package engine.test.render;

import java.util.ArrayList;
import java.util.List;

import commons.Resource;
import commons.ResourceLocator.ClasspathResourceLocator;
import commons.Transform2f;
import commons.matrix.Vector2f;
import commons.matrix.Vector3f;

import engine.core.Entity;
import engine.core.EntityBuilder;
import engine.core.Game;
import engine.core.Scene;
import engine.core.script.XPython;
import engine.imp.render.Animation;
import engine.imp.render.AnimationSystem;
import engine.imp.render.CAnimation;
import engine.imp.render.CCamera;
import engine.imp.render.CLight;
import engine.imp.render.CRender;
import engine.imp.render.LightFactory;
import engine.imp.render.LightingSystem;
import engine.imp.render.MaterialFactory;
import engine.imp.render.RenderingSystem;
import glcommon.Color;
import glextra.material.Material;
import gltools.texture.Texture2D;

/**
 * Tests the rendering. Uses art from the public domain Glitch game. THIS IS NOT A GOOD WAY TO LEARN HOW TO STRUCTURE
 * YOUR PROJECT. Look at the platformer game for that (seperate project).
 */
public class RenderTest {
	private MaterialFactory m_factory;
	private Resource m_script;

	private Resource m_rIcicle;
	private Material m_icicle;
	private Resource m_rLake;
	private Material m_lake;
	private Resource m_rPlatform1;
	private Material m_platform1;
	private Resource m_rPlatform4;
	private Material m_platform4;

	private Resource m_rFrame1;
	private Resource m_rFrame2;
	private Resource m_rFrame3;
	private Resource m_rFrame4;

	private Animation m_animation;

	public RenderTest() {
		ClasspathResourceLocator locator = new ClasspathResourceLocator();
		m_script = new Resource(locator, "engine/test/render/Script.py");
		m_rIcicle = new Resource(locator, "engine/test/render/cave_icicle_1_0.png");
		m_rLake = new Resource(locator, "engine/test/render/cave_lake_1_0.png");
		m_rPlatform1 = new Resource(locator, "engine/test/render/cave_platform_1_0.png");
		m_rPlatform4 = new Resource(locator, "engine/test/render/cave_platform_4_0.png");
		m_rFrame1 = new Resource(locator, "engine/test/render/treads1.png");
		m_rFrame2 = new Resource(locator, "engine/test/render/treads2.png");
		m_rFrame3 = new Resource(locator, "engine/test/render/treads3.png");
		m_rFrame4 = new Resource(locator, "engine/test/render/treads4.png");

	}

	public void start() {
		Game game = new Game();
		AnimationSystem animation = new AnimationSystem();
		RenderingSystem rendering = new RenderingSystem(2f, 2f);
		LightingSystem lighting = new LightingSystem(rendering);
		game.addSystem(animation);
		game.addSystem(rendering);
		game.addSystem(lighting);

		m_factory = new MaterialFactory(rendering);
		m_icicle = m_factory.createLighted(m_rIcicle);
		m_lake = m_factory.createLighted(m_rLake);
		m_platform1 = m_factory.createLighted(m_rPlatform1);
		m_platform4 = m_factory.createLighted(m_rPlatform4);

		List<Texture2D> frames = new ArrayList<Texture2D>();
		frames.add(m_factory.createTexture(m_rFrame1));
		frames.add(m_factory.createTexture(m_rFrame2));
		frames.add(m_factory.createTexture(m_rFrame3));
		frames.add(m_factory.createTexture(m_rFrame4));

		m_animation = new Animation(frames);
		m_animation.setRepeating(true);
		m_animation.setTimePerFrame(100f);

		Scene scene = new Scene(game);
		game.scenes().addScene(scene, "main");

		EntityBuilder lightBuilder = new EntityBuilder();
		lightBuilder.addComponentBuilder(new CLight(LightFactory.createDiffusePoint(new Vector3f(0f, 0f, 1f),
				new Vector3f(0.5f, 0.5f, 0.5f), new Color(0.7f, 0.7f, 1f))));
		scene.createEntity("light", scene, lightBuilder);

		EntityBuilder bIcicle = new EntityBuilder();
		bIcicle.addComponentBuilder(new CRender(m_icicle, 2, 1f));
		bIcicle.addScript(new XPython(m_script));

		EntityBuilder bLake = new EntityBuilder();
		CRender clake = new CRender(m_lake, 2, 1f);
		clake.setRepeatX(2);
		clake.setRepeatY(2);
		bLake.addComponentBuilder(clake);

		EntityBuilder bBackground = new EntityBuilder();

		EntityBuilder bPlatform1 = new EntityBuilder();
		bPlatform1.addComponentBuilder(new CRender(m_platform1, 0, 2f));
		bPlatform1.setTransform(new Transform2f(new Vector2f(-0.3f, 0f), 0f, new Vector2f(2f, 2f)));

		EntityBuilder bPlatform2 = new EntityBuilder();
		bPlatform2.addComponentBuilder(new CRender(m_platform4, 0, 2f));
		bPlatform2.setTransform(new Transform2f(new Vector2f(1f, 0f), 0f, new Vector2f(2f, 2f)));

		bBackground.addChildBuilder("platform1", bPlatform1);
		bBackground.addChildBuilder("platform2", bPlatform2);

		Entity background = scene.createEntity("background", scene, bBackground);
		background.getCTransform().translate(1f, 0f);

		Entity icicle = scene.createEntity("icicle1", scene, bIcicle);
		icicle.getCTransform().setTransform(
				new Transform2f(new Vector2f(0.5f, 0.6f), (float) Math.PI, new Vector2f(0.5f, 1f)));

		Entity lake = scene.createEntity("lake", scene, bLake);
		lake.getCTransform().translate(0f, -0.8f);

		EntityBuilder bTreads = new EntityBuilder();
		CAnimation animator = new CAnimation();
		animator.addAnimation("glow", m_animation);
		animator.playAnimation("glow");
		bTreads.addComponentBuilder(animator);
		bTreads.addComponentBuilder(new CRender(m_factory.create(), 1, 1.5f));
		scene.createEntity("treads", scene, bTreads);

		EntityBuilder cameraBuilder = new EntityBuilder();
		cameraBuilder.addComponentBuilder(new CCamera(0.5f, true));
		scene.createEntity("camera", scene, cameraBuilder);

		game.start();

		while (true) {
			game.update(16f);
		}
	}

	public static void main(String[] args) {
		new RenderTest().start();
	}
}
