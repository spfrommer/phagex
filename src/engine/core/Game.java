package engine.core;

import commons.Transform2f;
import commons.matrix.Vector2f;

import engine.core.script.SJava;

/**
 * Contains the Scenes and the Systems.
 */
public class Game {
	private SceneManager m_scenes;

	/**
	 * Initializes a Game with no Scenes.
	 */
	public Game() {
		m_scenes = new SceneManager();
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
		m_scenes.getCurrentScene().update(time);
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
		game.scenes().addScene("main", scene);

		Entity test = scene.createEntity("test", scene);
		test.setTransform(new Transform2f(new Vector2f(1f, 0f), 0f, new Vector2f(1f, 1f)));
		test.scripts().add(new SJava());

		Entity child = scene.createEntity("child1", test);
		child.setTransform(new Transform2f(new Vector2f(-1f, -1f), 0f, new Vector2f(1f, 1f)));

		child.setName("child");

		game.start();
		game.update(0.1666f);
	}
}
