package engine.core;

import java.util.ArrayList;
import java.util.List;

import commons.Transform2f;
import commons.matrix.Vector2f;

/**
 * Contains the Scenes and the Systems.
 */
public class Game {
	private SceneManager m_scenes;
	private List<EntitySystem> m_systems;

	/**
	 * Initializes a Game with no Scenes.
	 */
	public Game() {
		m_scenes = new SceneManager();
		m_systems = new ArrayList<EntitySystem>();
	}

	/**
	 * Starts the Game and loads the Scene called "main".
	 */
	public void start() {
		m_scenes.setCurrentScene("main");
	}

	/**
	 * Updates the current Scene.
	 * 
	 * @param time
	 */
	public void update(float time) {
		Scene current = m_scenes.getCurrentScene();
		List<Entity> entities = current.getAllEntities();

		for (EntitySystem system : m_systems)
			system.update(time);

		for (Entity e : entities) {
			for (EntitySystem system : m_systems) {
				if (system.getFilter().matches(e))
					system.updateEntity(e);
			}
		}

		// updates the Scripts
		m_scenes.getCurrentScene().updateScripts(time);
	}

	/**
	 * The SceneManager.
	 * 
	 * @return
	 */
	public SceneManager scenes() {
		return m_scenes;
	}

	public static void main(String[] args) {
		Game game = new Game();
		Scene scene = new Scene(game);
		game.scenes().addScene(scene, "main");

		Entity test = scene.createEntity("test", scene);
		test.getCTransform().setTransform(new Transform2f(new Vector2f(1f, 0f), 0f, new Vector2f(1f, 1f)));

		Entity child = scene.createEntity("child1", test);
		child.getCTransform().setTransform(new Transform2f(new Vector2f(-1f, -1f), 0f, new Vector2f(1f, 1f)));
		child.getCTags().addTag("foo");
		child.getCTags().addTag("bar");

		child.setName("child");

		game.start();
		game.update(0.1666f);
	}
}
