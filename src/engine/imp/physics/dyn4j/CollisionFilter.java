package engine.imp.physics.dyn4j;

import org.dyn4j.dynamics.contact.ContactConstraint;

import engine.core.Entity;

/**
 * Determines whether or not a collision should occur.
 */
public interface CollisionFilter {
	/**
	 * Returns whether the collision should happen.
	 * 
	 * @param entity1
	 *            the Entity the CollisionFilter belongs to
	 * @param entity2
	 *            the other Entity
	 * @return whether the collision should happen - used for high-level filtering
	 */
	public boolean canCollide(Entity entity1, Entity entity2);

	/**
	 * Called if the filter hasn't stopped the collision outright, but certain collision conditions might invalidate it.
	 * For example, if the player is jumping up through a platform, you might want to ignore the collision.
	 * 
	 * @param entity1
	 *            the Entity the CollisionFilter belongs to
	 * @param entity2
	 *            the other Entity
	 * @param constraint
	 *            the contact data
	 * @return whether the collision should continue given the specific ContactConstraint
	 */
	public boolean continueCollision(Entity entity1, Entity entity2, ContactConstraint constraint);
}
