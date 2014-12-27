package engine.core;

import java.util.ArrayList;
import java.util.List;

import commons.Transform2f;

import engine.core.exceptions.EntityException;
import engine.core.script.XScript;

/**
 * An Entity in a Scene. Child Entities all transform relative to each other.
 */
public class Entity implements TreeNode {
	// the Scene this Entity belongs to
	private Scene m_scene;

	// manages the children and parent
	private TreeManager m_tree;
	// manages the components
	private ComponentManager m_components;
	// manages the Scripts
	private ScriptManager m_scripts;

	private CTags m_tags;
	// the Entity's transformation in a 2d space as a component.
	private CTransform m_transform;
	// the Entity's shared script data
	private CScriptData m_scriptData;
	// listeners for transformation / child changes
	private List<EntityListener> m_listeners;

	// the name of the Entity
	private String m_name;

	/**
	 * Constructs a new Entity.
	 * 
	 * @param parent
	 * @param components
	 */
	protected Entity(String name, Scene scene, TreeNode parent, List<Component> components) {
		m_name = name;
		m_scene = scene;
		m_listeners = new ArrayList<EntityListener>();
		m_tags = new CTags(new TagList());
		m_transform = new CTransform(this, new Transform2f());
		m_scriptData = new CScriptData();

		m_tree = new TreeManager(this, parent);
		m_components = new ComponentManager(this, components);
		m_scripts = new ScriptManager(this);
	}

	/**
	 * Constructs a new Entity.
	 * 
	 * @param parent
	 * @param components
	 */
	protected Entity(String name, Scene scene, TreeNode parent, List<Component> components, TagList tags, Transform2f transform) {
		m_name = name;
		m_scene = scene;
		m_listeners = new ArrayList<EntityListener>();
		m_scriptData = new CScriptData();
		m_tags = new CTags(tags);
		m_transform = new CTransform(this, transform);

		m_tree = new TreeManager(this, parent);
		m_components = new ComponentManager(this, components);
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
	public void updateScripts(float time) {
		m_scripts.update(time);
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
	 * @return the listeners on this Entity
	 */
	protected List<EntityListener> getListeners() {
		return m_listeners;
	}

	/**
	 * Adds an EntityListener to the Entity.
	 * 
	 * @param listener
	 */
	public void addListener(EntityListener listener) {
		if (listener == null)
			throw new EntityException("Cannot add a null EntityListener!");

		m_listeners.add(listener);
	}

	/**
	 * Removes an EntityListener to the Entity.
	 * 
	 * @param listener
	 */
	public void removeListener(EntityListener listener) {
		if (listener == null)
			throw new EntityException("Cannot remove a null EntityListener!");
		if (!m_listeners.contains(listener))
			throw new EntityException("Tried to remove a nonexistant EntityListener!");

		m_listeners.add(listener);
	}

	@Override
	public String getName() {
		return m_name;
	}

	/**
	 * Sets the name of the Entity.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		m_tree.getParent().childNameChanged(this, m_name, name);
		m_name = name;
	}

	@Override
	public void childNameChanged(TreeNode child, String oldName, String newName) {
		m_tree.childNameChanged(child, oldName, newName);
	}

	/**
	 * @return the Scene this Entity belongs to
	 */
	public Scene getScene() {
		return m_scene;
	}

	/**
	 * @return the CTags
	 */
	public CTags tags() {
		return m_tags;
	}

	/**
	 * @return the CTransform
	 */
	public CTransform transform() {
		return m_transform;
	}

	/**
	 * @return the CScriptData
	 */
	public CScriptData scriptData() {
		return m_scriptData;
	}

	/**
	 * Creates a builder from the entity
	 * return
	 */
	public EntityBuilder getBuilder() {
		EntityBuilder builder = new EntityBuilder();

		builder.setTagList(getCTags().getTags());
		builder.setTransform(getCTransform().getTransform());

		
		for (XScript s : scripts().getAllScripts()) {
			builder.addScript(s);
		}
		for (Component c : components().all()) {
			builder.addComponentBuilder(c);
		}
		for (Entity child : tree().getChildren()) {
			builder.addChildBuilder(child.getName(), child.getBuilder());
		}
		
		return builder;
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

	@Override
	public String toString() {
		return m_name;
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
