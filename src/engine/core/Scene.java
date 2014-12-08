package engine.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import commons.Transform2f;
import commons.matrix.Matrix;
import commons.matrix.MatrixFactory;
import commons.matrix.Vector2f;

import engine.core.exceptions.EntityException;
import engine.core.exceptions.SceneException;

/**
 * Creates, destroys, and moves Entities in the tree.
 */
public class Scene implements TreeNode {
	// the top level entities are practically the children of the EntityContainer aspect of the Scene
	private List<Entity> m_rootEntities;
	private List<Entity> m_allEntities;
	private List<String> m_childNames;
	private Game m_game;

	private String m_name;

	/**
	 * Initializes a Scene in a Game.
	 * 
	 * @param game
	 */
	public Scene(Game game) {
		m_rootEntities = new ArrayList<Entity>();
		m_allEntities = new ArrayList<Entity>();
		m_childNames = new ArrayList<String>();
		m_game = game;
	}

	/**
	 * Calls onSceneLoad() on all the Entities.
	 */
	public void onSceneLoad() {
		for (Entity entity : m_allEntities)
			entity.onSceneLoad();
	}

	/**
	 * Updates the Entities in the Scene.
	 * 
	 * @param time
	 */
	public void updateScripts(float time) {
		for (Entity entity : m_allEntities)
			entity.updateScripts(time);
	}

	/**
	 * @return the Game this Scene belongs to
	 */
	public Game getGame() {
		return m_game;
	}

	/**
	 * Creates a child Entity of a parent container. The Entity is empty.
	 * 
	 * @param name
	 * 
	 * @param parent
	 * @return
	 */
	public Entity createEntity(String name, TreeNode parent) {
		if (parent == null)
			throw new SceneException("Cannot create an Entity with a null parent!");
		if (name == null)
			throw new SceneException("Cannot create an Entity with a null name!");
		if (!m_allEntities.contains(parent) && !(this == parent))
			throw new SceneException("Trying to create an Entity in a container not in the Scene!");
		Entity entity = new Entity(name, this, parent, new ArrayList<Component>());
		parent.addChild(entity);
		m_allEntities.add(entity);
		m_game.entityAdded(entity, parent, this);
		return entity;
	}

	/**
	 * Creates a child Entity of a parent container. The Entity has the Components specified in the builder as well as a
	 * CTransform and a CScriptData.
	 * 
	 * @param name
	 * 
	 * @param parent
	 * @param builder
	 * @return
	 */
	public Entity createEntity(String name, TreeNode parent, EntityBuilder builder) {
		if (parent == null)
			throw new SceneException("Cannot create an Entity with a null parent!");
		if (name == null)
			throw new SceneException("Cannot create an Entity with a null name!");
		if (!m_allEntities.contains(parent) && !(this == parent))
			throw new SceneException("Trying to create an Entity in a container not in the Scene!");

		List<Component> components = new ArrayList<Component>();
		for (ComponentBuilder<? extends Component> componentBuilder : builder.getComponentBuilders()) {
			components.add(componentBuilder.build());
		}

		Entity entity = new Entity(name, this, parent, components, builder.getTagList(), builder.getTransform());
		parent.addChild(entity);
		m_allEntities.add(entity);
		m_game.entityAdded(entity, parent, this);
		return entity;
	}

	/**
	 * Removes an Entity from the Scene.
	 * 
	 * @param entity
	 * @param destroy
	 *            whether or not the Entity should be completely destroyed to speed up garbage collection. Unless you
	 *            want to reuse the Entity later, set this flag to true.
	 */
	public void destroyEntity(Entity entity, boolean destroy) {
		if (!m_allEntities.contains(entity))
			throw new SceneException("Trying to destroy an Entity not in the Scene!");

		entity.tree().getParent().removeChild(entity);
		recursiveRemove(entity, entity.tree().getParent(), destroy);
	}

	/**
	 * Recursively removes the Entity and its children from the allEntities Set. Will also call destroy() on the
	 * Entities.
	 * 
	 * @param entity
	 */
	private void recursiveRemove(Entity entity, TreeNode parent, boolean destroy) {
		for (Entity child : entity.tree().getChildren()) {
			recursiveRemove(child, entity, destroy);
		}
		m_allEntities.remove(entity);
		if (destroy)
			entity.destroy();
		m_game.entityRemoved(entity, parent, this);
	}

	/**
	 * Moves an Entity to a new parent.
	 * 
	 * @param entity
	 * @param newParent
	 */
	public void moveEntity(Entity entity, TreeNode newParent) {
		if (entity == null)
			throw new SceneException("Tried to move a null Entity!");
		if (newParent == null)
			throw new SceneException("Tried to move an Entity into a null parent!");
		if (!m_allEntities.contains(newParent) && !(this == newParent))
			throw new SceneException("Tried to move an Entity to a container not in the Scene!");
		if (entity.tree().getParent() == newParent)
			throw new SceneException("Tried to move an Entity into its parent!");

		TreeNode oldParent = entity.tree().getParent();

		Transform2f oldWorldTrans = getWorldTransform(entity);
		Transform2f oldLocalTrans = entity.getCTransform().getTransform();

		Transform2f newParentWorldTrans;
		if (newParent == this) {
			newParentWorldTrans = new Transform2f();
		} else {
			newParentWorldTrans = getWorldTransform(newParent);
		}

		Transform2f newLocalTrans = new Transform2f();
		newLocalTrans.setTranslation(oldWorldTrans.getTranslation().subtract(newParentWorldTrans.getTranslation())
				.toVector2f());
		newLocalTrans.setRotation(newLocalTrans.getRotation() - oldWorldTrans.getRotation());
		Vector2f oldScale = oldWorldTrans.getScale();
		Vector2f chainScale = newParentWorldTrans.getScale();
		newLocalTrans.setScale(new Vector2f(oldScale.getX() / chainScale.getX(), oldScale.getY() / chainScale.getY()));

		oldParent.removeChild(entity);
		newParent.addChild(entity);
		entity.tree().setParent(newParent/*, newLocalTrans*/);
		entity.getCTransform().quietSetTransform(newLocalTrans);

		Transform2f newWorldTrans = getWorldTransform(entity);

		for (EntityListener listener : entity.getListeners())
			listener.parentChanged(entity, oldParent, newParent, oldLocalTrans, newLocalTrans, oldWorldTrans,
					newWorldTrans, this);
		m_game.entityMoved(entity, oldParent, newParent, this);
	}

	/**
	 * @param node
	 * @return the transform of the Entity in world coordinates.
	 */
	public Transform2f getWorldTransform(TreeNode node) {
		if (node == null)
			throw new SceneException("Tried to transform a null parent!");
		if (node instanceof Scene)
			return new Transform2f();
		if (!m_allEntities.contains(node))
			throw new SceneException("Entity is not part of Scene - transform cannot be calculated!");
		// List of entities in the chain
		List<Entity> entities = new ArrayList<Entity>();
		TreeNode parent = node;
		while (parent != this) {
			entities.add((Entity) parent);
			parent = ((Entity) parent).tree().getParent();
		}
		Collections.reverse(entities);

		Matrix transMatrix = MatrixFactory.identity(3);
		for (Entity e : entities) {
			Transform2f trans = e.getCTransform().getTransform();
			// translate
			transMatrix = transMatrix.multiply(MatrixFactory.affineTranslate(trans.getTranslation()));
			// rotate
			transMatrix = transMatrix.multiply(MatrixFactory.affineRotation(trans.getRotation()));
			// scale
			transMatrix = transMatrix.multiply(MatrixFactory.affineScale(trans.getScale()));
		}

		return new Transform2f(transMatrix);
	}

	/**
	 * Gets all the Entities in the Scene.
	 * 
	 * @return
	 */
	protected List<Entity> directGetAllEntities() {
		return m_allEntities;
	}

	/**
	 * Gets all the Entities in the Scene.
	 * 
	 * @return
	 */
	public List<Entity> getAllEntities() {
		return new ArrayList<Entity>(m_allEntities);
	}

	/**
	 * Gets the top level Entities in the Scene.
	 * 
	 * @return
	 */
	protected List<Entity> directGetRootEntities() {
		return m_rootEntities;
	}

	/**
	 * Gets all the top level Entities in the Scene.
	 * 
	 * @return
	 */
	public List<Entity> getRootEntities() {
		return new ArrayList<Entity>(m_rootEntities);
	}

	/**
	 * Adds a TOP-LEVEL child to the Scene. This should not be called. Use createEntity(name, scene) instead.
	 */
	@Override
	public void addChild(Entity entity) {
		if (m_rootEntities.contains(entity))
			throw new SceneException("Entity is already in Scene!");
		if (m_childNames.contains(entity.getName()))
			throw new EntityException("No two children with the same name allowed!");
		m_childNames.add(entity.getName());
		m_rootEntities.add(entity);
	}

	/**
	 * Removes a TOP-LEVEL child from the Scene. This should not be called. You should call destroyEntity(Entity) if you
	 * want to completely destroy it.
	 */
	@Override
	public void removeChild(Entity entity) {
		if (!m_rootEntities.contains(entity))
			throw new SceneException("Trying to remove an Entity not in the top level!");
		m_childNames.remove(entity.getName());
		m_rootEntities.remove(entity);
	}

	@Override
	public void childNameChanged(TreeNode child, String oldName, String newName) {
		if (m_childNames.contains(newName))
			throw new EntityException("No two children with the same name allowed!");
		m_childNames.remove(oldName);
		m_childNames.add(newName);
	}

	@Override
	public String getName() {
		return m_name;
	}

	/**
	 * Sets the name of the Scene.
	 * 
	 * @param name
	 */
	protected void setName(String name) {
		m_name = name;
	}
}
