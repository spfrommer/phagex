package engine.core;

import java.util.ArrayList;
import java.util.List;

import commons.Transform2f;

import engine.core.exceptions.ComponentException;

/**
 * An Entity in a Scene. Child Entities all transform relative to each other.
 */
public class Entity implements EntityContainer {
	// the Scene this Entity belongs to
	private Scene m_scene;

	// manages the children and parent
	private TreeManager m_tree;
	// manages the components and fields
	private ComponentManager m_components;
	// manages the Scripts
	private ScriptManager m_scripts;

	// the Entity's transformation in a 2d space as a component.
	private CTransform m_transform;
	// the Entity's shared script data
	private CScriptData m_scriptData;
	// listeners for transformation / child changes
	private List<EntityListener> m_listeners;

	/**
	 * Constructs a new Entity.
	 * 
	 * @param scene
	 * @param parent
	 * @param components
	 */
	protected Entity(Scene scene, EntityContainer parent, List<Component> components) {
		m_scene = scene;
		m_listeners = new ArrayList<EntityListener>();
		m_transform = new CTransform(this, new Transform2f());
		m_scriptData = new CScriptData();

		m_tree = new TreeManager(this, parent);
		m_components = new ComponentManager(components, m_transform, m_scriptData);
		m_scripts = new ScriptManager(this);
	}

	/**
	 * Calls onSceneLoad() on the Scripts.
	 */
	public void onSceneLoad() {
		m_scripts.onSceneLoad();
	}

	/**
	 * Updates the Entity's Scripts and reloads the script data.
	 * 
	 * @param time
	 */
	public void update(float time) {
		m_scripts.update(time);
		m_components.reloadData(m_scriptData);
	}

	/**
	 * @return the listeners on this Entity
	 */
	protected List<EntityListener> getListeners() {
		return m_listeners;
	}

	/**
	 * Destroys the instance fields of the Entity to make garbage collection easier.
	 */
	protected void destroy() {
		m_scene = null;
		m_transform = null;
		m_listeners.clear();
	}

	/**
	 * @return the Scene this Entity belongs to
	 */
	public Scene getScene() {
		return m_scene;
	}

	/**
	 * Do not modify this transform as it will not notify the listeners, and the physics system will simply overwrite
	 * your new values. Call setTransform() instead.
	 * 
	 * @return the Entity's transform.
	 */
	public Transform2f getTransform() {
		return m_transform.getTransform();
	}

	/**
	 * Sets the Entity's transform and notifies its listeners. These listeners are only notified if the transform is
	 * changed without changing the parents.
	 * 
	 * @param transform
	 */
	public void setTransform(Transform2f transform) {
		if (transform == null)
			throw new ComponentException("Cannot set a null transform!");

		m_transform.setTransform(transform);
	}

	/**
	 * Returns the TreeManager, which manages this Entity's children and parents.
	 * 
	 * @return
	 */
	public TreeManager tree() {
		return m_tree;
	}

	/**
	 * Returns the ComponentManager, which manages this Entity's components.
	 * 
	 * @return
	 */
	public ComponentManager components() {
		return m_components;
	}

	/**
	 * Returns the ScriptManager, which manages this Entity's scripts.
	 * 
	 * @return
	 */
	public ScriptManager scripts() {
		return m_scripts;
	}

	private String toTabbedString(int tabs) {
		String tabString = makeTabs(tabs);
		String string = "";
		string += tabString + "Entity " + super.toString() + ": \n";
		for (Entity e : m_tree.getChildren()) {
			string += /*tabString + "Child: \n" + */e.toTabbedString(tabs + 1);
		}
		return string;
	}

	private String makeTabs(int tabs) {
		String string = "";
		for (int i = 0; i < tabs; i++) {
			string += "\t";
		}
		return string;
	}

	@Override
	public String toString() {
		String string = "";
		string += "\n----------------------------------------------\n";
		string += "Entity " + super.toString() + ": \n";
		for (Entity e : m_tree.getChildren()) {
			string += /*"Child: \n" + */e.toTabbedString(1);
		}
		string += "\n----------------------------------------------";
		return string;
	}

	@Override
	public void addChild(Entity entity) {
		m_tree.addChild(entity);
	}

	@Override
	public void removeChild(Entity entity) {
		m_tree.removeChild(entity);
	}
}
