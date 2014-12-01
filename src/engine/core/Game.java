package engine.core;

import java.util.ArrayList;
import java.util.List;

import engine.core.exceptions.GameException;

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
		for (EntitySystem system : m_systems)
			system.postUpdate();
		// updates the Scripts
		m_scenes.getCurrentScene().updateScripts(time);
	}

	/**
	 * Adds an EntitySystem to the Game.
	 * 
	 * @param system
	 */
	public void addSystem(EntitySystem system) {
		if (m_systems.contains(system))
			throw new GameException("Cannot add the same EntitySystem twice!");
		m_systems.add(system);
	}

	/**
	 * Removes an EntitySystem from the Game.
	 * 
	 * @param system
	 */
	public void removeSystem(EntitySystem system) {
		if (!m_systems.contains(system))
			throw new GameException("Cannot remove a nonexistant EntitySystem!");
		m_systems.remove(system);
	}

	/**
	 * The SceneManager.
	 * 
	 * @return
	 */
	public SceneManager scenes() {
		return m_scenes;
	}
}
