package engine.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import commons.Transform2f;
import commons.matrix.Matrix;
import commons.matrix.MatrixFactory;
import commons.matrix.Vector2f;

/**
 * Creates, destroys, and moves Entities in the tree.
 */
public class Scene implements EntityContainer {
	// the top level entities are practically the children of the EntityContainer aspect of the Scene
	private Set<Entity> m_topLevelEntities;
	private Set<Entity> m_allEntities;

	public Scene() {
		m_topLevelEntities = new LinkedHashSet<Entity>();
		m_allEntities = new LinkedHashSet<Entity>();
	}

	/**
	 * Creates a child Entity of a parent.
	 * 
	 * @param parent
	 * @return
	 */
	public Entity createEntity(EntityContainer parent) {
		if (parent == null)
			throw new SceneException("Cannot create an Entity with a null parent!");
		if (!m_allEntities.contains(parent) && !(this == parent))
			throw new SceneException("Trying to create an Entity in a container not in the Scene!");
		Entity entity = new Entity(this, parent);
		parent.addChild(entity);
		m_allEntities.add(entity);
		return entity;
	}

	/**
	 * Removes an Entity from the Scene.
	 * 
	 * @param entity
	 */
	public void destroyEntity(Entity entity) {
		if (!m_allEntities.contains(entity))
			throw new SceneException("Trying to destroy an Entity not in the Scene!");

		entity.getParent().removeChild(entity);
		recursiveRemove(entity);
	}

	/**
	 * Recursively removes the Entity and its children from the allEntities Set. Will also call destroy() on the
	 * Entities.
	 * 
	 * @param entity
	 */
	private void recursiveRemove(Entity entity) {
		for (Entity child : entity.getChildren()) {
			recursiveRemove(child);
		}
		m_allEntities.remove(entity);
		entity.destroy();
	}

	/**
	 * Moves an Entity to a new parent.
	 * 
	 * @param entity
	 * @param newParent
	 */
	public void moveEntity(Entity entity, EntityContainer newParent) {
		if (entity == null)
			throw new SceneException("Tried to move a null Entity!");
		if (newParent == null)
			throw new SceneException("Tried to move an Entity into a null parent!");
		if (!m_allEntities.contains(newParent) && !(this == newParent))
			throw new SceneException("Tried to move an Entity to a container not in the Scene!");
		if (entity.getParent() == newParent)
			throw new SceneException("Tried to move an Entity into its parent!");
		Transform2f oldTrans = getWorldTransform(entity);
		System.out.println("Old trans: \n" + oldTrans);
		Transform2f chainTrans;
		if (newParent == this) {
			chainTrans = new Transform2f();
		} else {
			chainTrans = getWorldTransform((Entity) newParent);
		}

		System.out.println("Chain trans: \n" + chainTrans);

		Transform2f newTrans = new Transform2f();
		newTrans.setTranslation(oldTrans.getTranslation().subtract(chainTrans.getTranslation()).toVector2f());
		newTrans.setRotation(newTrans.getRotation() - oldTrans.getRotation());
		Vector2f oldScale = oldTrans.getScale();
		Vector2f chainScale = chainTrans.getScale();
		newTrans.setScale(new Vector2f(oldScale.getX() / chainScale.getX(), oldScale.getY() / chainScale.getY()));

		System.out.println("New trans: \n" + newTrans);

		entity.getParent().removeChild(entity);
		newParent.addChild(entity);
		entity.setParent(newParent, newTrans);
	}

	public Transform2f getWorldTransform(Entity entity) {
		if (entity == null)
			throw new SceneException("Tried to transform a null parent!");
		if (!m_allEntities.contains(entity))
			throw new SceneException("Entity is not part of Scene - transform cannot be calculated!");
		// List of entities in the chain
		List<Entity> entities = new ArrayList<Entity>();
		EntityContainer parent = entity;
		while (parent != this) {
			entities.add((Entity) parent);
			parent = ((Entity) parent).getParent();
		}
		Collections.reverse(entities);

		Matrix transMatrix = MatrixFactory.identity(3);
		for (Entity e : entities) {
			Transform2f trans = e.getTransform();
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
	 * Adds a TOP-LEVEL child to the Scene. This should not be called. Use createEntity() instead.
	 */
	@Override
	public void addChild(Entity entity) {
		if (m_topLevelEntities.contains(entity))
			throw new SceneException("Entity is already in Scene!");
		m_topLevelEntities.add(entity);
	}

	/**
	 * Removes a TOP-LEVEL child from the Scene. Really, you should call destroyEntity(Entity) if you want to completely
	 * destroy it.
	 */
	@Override
	public void removeChild(Entity entity) {
		if (!m_topLevelEntities.contains(entity))
			throw new SceneException("Trying to remove an Entity not in the top level!");
		m_topLevelEntities.remove(entity);
	}

	public static void main(String[] args) {
		Scene scene = new Scene();
		Entity topLevel = scene.createEntity(scene);
		Entity secondLevel1 = scene.createEntity(topLevel);
		Entity secondLevel2 = scene.createEntity(topLevel);

		Entity thirdLevel = scene.createEntity(secondLevel2);

		System.out.println("Top level: " + topLevel.hashCode());
		System.out.println("Second level 1: " + secondLevel1.hashCode());
		System.out.println("Second level 2: " + secondLevel2.hashCode());
		System.out.println("Third level: " + thirdLevel.hashCode() + "\n");

		topLevel.setTransform(new Transform2f(new Vector2f(1f, 0f), 0f, new Vector2f(1f, 1f)));
		secondLevel2.setTransform(new Transform2f(new Vector2f(5f, 0f), 0f, new Vector2f(1f, 1f)));
		thirdLevel.setTransform(new Transform2f(new Vector2f(0f, 1f), 0f, new Vector2f(1f, 1f)));

		// System.out.println(thirdLevel.getTransform().getTranslation());
		// System.out.println(scene.getWorldTransform(thirdLevel).getTranslation() + "\n");

		scene.moveEntity(thirdLevel, secondLevel1);
		// System.out.println(thirdLevel.getParent().hashCode());
		System.out.println("------------------------");
		System.out.println(scene.getWorldTransform(thirdLevel).getTranslation());
		System.out.println(thirdLevel.getTransform().getTranslation());
	}
}
