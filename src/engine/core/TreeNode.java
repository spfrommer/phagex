package engine.core;

/**
 * A node in the tree - usually a Scene or an Entity.
 */
public interface TreeNode {
	/**
	 * Adds an Entity to the container.
	 * 
	 * @param entity
	 */
	public void addChild(Entity entity);

	/**
	 * Removes an Entity from the container.
	 * 
	 * @param entity
	 */
	public void removeChild(Entity entity);

	/**
	 * Returns the name of the TreeNode.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Called by the child when its name changes.
	 * 
	 * @param child
	 * @param oldName
	 * @param newName
	 */
	public void childNameChanged(TreeNode child, String oldName, String newName);
}
