package engine.core;

import java.util.HashMap;
import java.util.Map;

import engine.core.exceptions.SceneException;

/**
 * Manages unused Scenes for the Game.
 */
public class SceneManager {
	private Map<String, Scene> m_scenes;

	private Scene m_current;

	public SceneManager() {
		m_scenes = new HashMap<String, Scene>();
	}

	/**
	 * Adds a Scene to the game.
	 * 
	 * @param name
	 * @param scene
	 */
	public void addScene(String name, Scene scene) {
		if (m_scenes.containsKey(name))
			throw new SceneException("Scene already exists for: " + name);
		if (scene == null)
			throw new SceneException("Tried to add null Scene!");
		m_scenes.put(name, scene);
	}

	/**
	 * Removes a Scene from the game.
	 * 
	 * @param name
	 */
	public void removeScene(String name) {
		if (!m_scenes.containsKey(name))
			throw new SceneException("No Scene found for: " + name);
		if (m_current == m_scenes.get(name))
			throw new SceneException("Cannot remove current Scene!");
		m_scenes.remove(name);
	}

	/**
	 * Returns whether the game has a Scene of this name.
	 * 
	 * @param name
	 * @return
	 */
	public boolean hasScene(String name) {
		return m_scenes.containsKey(name);
	}

	/**
	 * Sets the current Scene.
	 * 
	 * @param name
	 */
	public void setCurrentScene(String name) {
		if (!m_scenes.containsKey(name))
			throw new SceneException("No Scene found for: " + name);
		m_current = m_scenes.get(name);
	}

	/**
	 * Returns the current Scene;
	 * 
	 * @return
	 */
	public Scene getCurrentScene() {
		return m_current;
	}

	/**
	 * Prints out all the Scenes that are in the game to standard out.
	 */
	public void dumpScenes() {
		for (Scene scene : m_scenes.values())
			System.out.println(scene);
		System.out.println("\nCurrent: \n" + m_current);
	}
}
