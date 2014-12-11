package engine.imp.physics.dyn4j;

import org.dyn4j.dynamics.joint.DistanceJoint;
import org.dyn4j.dynamics.joint.RevoluteJoint;
import org.dyn4j.geometry.Vector2;

import commons.matrix.Vector2f;

import engine.core.Entity;
import engine.imp.physics.PhysicsException;
import engine.imp.physics.PhysicsUtils;

/**
 * Creates the simple Dyn4j physics elements needed for a game.
 */
public class PhysicsFactory {
	/**
	 * Creates a revolute joint between two Entities around a certain anchor specified in world coordinates.
	 * 
	 * @param body1
	 * @param body2
	 * @param worldAnchor
	 * @return
	 */
	public static RevoluteJoint createRevolute(Entity body1, Entity body2, Vector2f worldAnchor) {
		if (body1 == null || body2 == null)
			throw new PhysicsException("Cannot create revolute joint with null Body!");
		if (worldAnchor == null)
			throw new PhysicsException("Cannot create revolute joint with null Anchor!");

		CDyn4jBody b1 = (CDyn4jBody) body1.components().get(CDyn4jBody.NAME);
		CDyn4jBody b2 = (CDyn4jBody) body2.components().get(CDyn4jBody.NAME);
		Vector2 a = PhysicsUtils.toDyn4j(worldAnchor);
		return new RevoluteJoint(b1.getBody(), b2.getBody(), a);
	}

	/**
	 * Creates a DistanceJoint - this tries to maintain the distance between the two anchors specified in world
	 * coordinates.
	 * 
	 * @param body1
	 * @param body2
	 * @param worldAnchor1
	 * @param worldAnchor2
	 * @return
	 */
	public static DistanceJoint createDistance(Entity body1, Entity body2, Vector2f worldAnchor1, Vector2f worldAnchor2) {
		if (body1 == null || body2 == null)
			throw new PhysicsException("Cannot create revolute joint with null Body!");

		CDyn4jBody b1 = (CDyn4jBody) body1.components().get(CDyn4jBody.NAME);
		CDyn4jBody b2 = (CDyn4jBody) body2.components().get(CDyn4jBody.NAME);
		Vector2 a1 = PhysicsUtils.toDyn4j(worldAnchor1);
		Vector2 a2 = PhysicsUtils.toDyn4j(worldAnchor2);
		return new DistanceJoint(b1.getBody(), b2.getBody(), a1, a2);
	}
}
