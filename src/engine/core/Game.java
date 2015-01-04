package engine.core;

import java.util.ArrayList;
import java.util.List;

import engine.core.exceptions.GameException;
import engine.core.script.XScript;

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
		m_systems = new ArrayList<EntitySystem>();
		m_scenes = new SceneManager(this);
	}

	/**
	 * Called by the Scene.
	 * 
	 * @param oldScene
	 * @param newScene
	 */
	protected void sceneChanged(Scene oldScene, Scene newScene) {
		for (EntitySystem system : m_systems) {
			system.sceneChanged(oldScene, newScene);
		}
	}

	/**
	 * Called by the Scene.
	 * 
	 * @param entity
	 * @param parent
	 * @param scene
	 */
	protected void entityAdded(Entity entity, TreeNode parent, Scene scene) {
		for (EntitySystem system : m_systems) {
			if (system.getEntityEventFilter().matches(entity))
				system.entityAdded(entity, parent, scene);
		}
	}

	/**
	 * Called by the Scene.
	 * 
	 * @param entity
	 * @param parent
	 * @param scene
	 */
	protected void entityRemoved(Entity entity, TreeNode parent, Scene scene) {
		for (EntitySystem system : m_systems) {
			if (system.getEntityEventFilter().matches(entity))
				system.entityRemoved(entity, parent, scene);
		}
	}

	/**
	 * Called by the Scene.
	 * 
	 * @param entity
	 * @param oldParent
	 * @param newParent
	 * @param scene
	 */
	protected void entityMoved(Entity entity, TreeNode oldParent, TreeNode newParent, Scene scene) {
		for (EntitySystem system : m_systems) {
			if (system.getEntityEventFilter().matches(entity))
				system.entityMoved(entity, oldParent, newParent, scene);
		}
	}

	/**
	 * Called by the Scene.
	 * 
	 * @param entity
	 * @param parent
	 * @param scene
	 */
	protected void entityEnabled(Entity entity, TreeNode parent, Scene scene) {
		for (EntitySystem system : m_systems) {
			if (system.getEntityEventFilter().matches(entity))
				system.entityEnabled(entity, parent, scene);
		}
	}

	/**
	 * Called by the Scene.
	 * 
	 * @param entity
	 * @param parent
	 * @param scene
	 */
	protected void entityDisabled(Entity entity, TreeNode parent, Scene scene) {
		for (EntitySystem system : m_systems) {
			if (system.getEntityEventFilter().matches(entity))
				system.entityDisabled(entity, parent, scene);
		}
	}

	/**
	 * Called by an Entity.
	 * 
	 * @param entity
	 * @param script
	 * @param scene
	 */
	protected void scriptAdded(Entity entity, XScript script, Scene scene) {
		for (EntitySystem system : m_systems) {
			system.scriptAdded(entity, script, scene);
		}
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

		for (EntitySystem system : m_systems)
			system.update(current, time);

		List<Entity> entities = current.getAllEntities();
		for (Entity e : entities) {
			if (e.isEnabled()) {
				for (EntitySystem system : m_systems) {
					if (system.getUpdateFilter().matches(e))
						system.updateEntity(e, current, time);
				}
			}
		}
		for (EntitySystem system : m_systems)
			system.postUpdate(current);

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
