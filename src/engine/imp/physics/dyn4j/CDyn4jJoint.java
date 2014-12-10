package engine.imp.physics.dyn4j;

import org.dyn4j.dynamics.joint.Joint;

import commons.Logger;
import commons.matrix.Vector2f;

import engine.core.Component;
import engine.core.ComponentBuilder;
import engine.core.exceptions.ComponentException;
import engine.imp.physics.PhysicsException;
import engine.imp.physics.PhysicsUtils;

public class CDyn4jJoint implements Component {
	public static final String NAME = "physics_joint";
	public static final String JOINT = "physics_joint";
	public static final String ANCHOR1 = "physics_anchor1";
	public static final String ANCHOR2 = "physics_anchor2";
	public static final String COLLISION_ALLOWED = "physics_collisionAllowed";

	public static final String[] IDENTIFIERS = new String[] { JOINT, ANCHOR1, ANCHOR2, COLLISION_ALLOWED };

	private Joint m_joint;

	public CDyn4jJoint(Joint joint) {
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
	public String[] getIdentifiers() {
		return IDENTIFIERS;
	}

	@Override
	public Object getData(String identifier) {
		if (identifier == null)
			throw new ComponentException("Cannot get data for null identifier!");

		if (identifier.equals(JOINT))
			return m_joint;
		if (identifier.equals(ANCHOR1))
			return getAnchor1();
		if (identifier.equals(ANCHOR2))
			return getAnchor2();
		if (identifier.equals(COLLISION_ALLOWED))
			return isCollisionAllowed();

		throw new ComponentException("No such identifier!");
	}

	@Override
	public void setData(String identifier, Object data) {
		if (identifier == null)
			throw new ComponentException("Cannot set data for null identifier!");
		if (data == null)
			throw new ComponentException("Cannot set null data for identifier: " + identifier);

		if (identifier.equals(JOINT)) {
			setJoint((Joint) data);
		} else if (identifier.equals(COLLISION_ALLOWED)) {
			setCollisionAllowed((Boolean) data);
		} else if (identifier.equals(ANCHOR1) || identifier.equals(ANCHOR2)) {
			Logger.instance().warn("Cannot set anchors in Joint!");
		} else {
			throw new ComponentException("No data for identifier: " + identifier);
		}
	}

	@Override
	public ComponentBuilder<CDyn4jJoint> getBuilder() {
		ComponentBuilder<CDyn4jJoint> builder = new ComponentBuilder<CDyn4jJoint>() {
			@Override
			public CDyn4jJoint build() {
				return new CDyn4jJoint(m_joint);
			}

			@Override
			public String getName() {
				return NAME;
			}
		};
		return builder;
	}
}
