package engine.imp.physics.dyn4j;

import org.dyn4j.dynamics.joint.Joint;

import commons.matrix.Vector2f;

import engine.core.Component;
import engine.core.ComponentBuilder;
import engine.imp.physics.PhysicsException;
import engine.imp.physics.PhysicsUtils;

public class CJoint implements Component {
	public static final String NAME = "joint";

	private Joint m_joint;

	public CJoint(Joint joint) {
		if (joint == null)
			throw new PhysicsException("Joint cannot be null!");
		m_joint = joint;
	}

	/**
	 * @return the Joint
	 */
	public Joint getJoint() {
		return m_joint;
	}

	/**
	 * Sets the Joint and overides all the anchors/iscollisionallowed.
	 * 
	 * @param joint
	 */
	public void setJoint(Joint joint) {
		if (joint == null)
			throw new PhysicsException("Cannot set null Joint!");

		m_joint = joint;
	}

	/**
	 * @return the first anchor
	 */
	public Vector2f getAnchor1() {
		return PhysicsUtils.fromDyn4j(m_joint.getAnchor1());
	}

	/**
	 * @return the second anchor
	 */
	public Vector2f getAnchor2() {
		return PhysicsUtils.fromDyn4j(m_joint.getAnchor2());
	}

	/**
	 * @return whether or not collision is allowed between the jointed Bodies
	 */
	public boolean isCollisionAllowed() {
		return m_joint.isCollisionAllowed();
	}

	/**
	 * Sets whether or not collision is allowed between the jointed Bodies.
	 * 
	 * @param collisionAllowed
	 */
	public void setCollisionAllowed(boolean collisionAllowed) {
		m_joint.setCollisionAllowed(collisionAllowed);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public ComponentBuilder<CJoint> getBuilder() {
		ComponentBuilder<CJoint> builder = new ComponentBuilder<CJoint>() {
			@Override
			public CJoint build() {
				return new CJoint(m_joint);
			}

			@Override
			public String getName() {
				return NAME;
			}
		};
		return builder;
	}
}
