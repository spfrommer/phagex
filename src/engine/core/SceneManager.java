package engine.core;

import java.util.HashMap;
import java.util.Map;

import commons.Logger;

import engine.core.exceptions.SceneException;

/**
 * Manages unused Scenes for the Game.
 */
public class SceneManager {
	private Map<String, Scene> m_scenes;

	private Scene m_current;

	/**
	 * Makes a new SceneManager.
	 */
	public SceneManager() {
		m_scenes = new HashMap<String, Scene>();
	}

	/**
	 * Adds a Scene to the game.
	 * 
	 * @param scene
	 * @param name
	 */
	public void addScene(Scene scene, String name) {
		if (m_scenes.containsKey(name))
			throw new SceneException("Scene already exists for: " + name);
		if (scene == null)
			throw new SceneException("Tried to add null Scene!");

		scene.setName(name);
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
	 * Sets the current Scene. DO NOT CALL THIS UNTIL YOU HAVE CALLED START() IN THE GAME.
	 * 
	 * @param name
	 */
	public void setCurrentScene(String name) {
		if (!m_scenes.containsKey(name))
			throw new SceneException("No Scene found for: " + name);
		m_current = m_scenes.get(name);
		m_scenes.get(name).onSceneLoad();
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
	 * Logs all the Scenes that are in the game.
	 */
	public void dumpScenes() {
		for (Scene scene : m_scenes.values())
			Logger.instance().debug("" + scene);
		Logger.instance().debug("\nCurrent: \n" + m_current);
	}
}
