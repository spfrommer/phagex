package engine.core;

import java.util.ArrayList;
import java.util.List;

import commons.Transform2f;

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
	// manages the fields
	private FieldManager m_fields;
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
	 * @param scene
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
		m_fields = new FieldManager(this);
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
		m_fields.reloadData(m_scriptData);
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

	public String name() {
		return m_name;
	}

	@Override
	public String getName() {
		return m_name;
	}

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
	public CTags getCTags() {
		return m_tags;
	}

	/**
	 * @return the CTransform
	 */
	public CTransform getCTransform() {
		return m_transform;
	}

	/**
	 * @return the CScriptData
	 */
	public CScriptData getCScriptData() {
		return m_scriptData;
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
	 * Returns the FieldManager, which manages this Entity's fields. It loads the fields from the ComponentManager.
	 * 
	 * @return
	 */
	public FieldManager fields() {
		return m_fields;
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
