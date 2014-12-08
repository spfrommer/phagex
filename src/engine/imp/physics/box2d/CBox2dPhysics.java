package engine.imp.physics.box2d;

import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;

import commons.Logger;
import commons.matrix.Vector2f;

import engine.core.Component;
import engine.core.ComponentBuilder;
import engine.core.exceptions.ComponentException;

/**
 * Contains data for the PhysicsSystem.
 */
public class CBox2dPhysics implements Component {
	public static final String NAME = "box2dphysics";
	// how the children should be linked to this Entity
	public static final String TYPE = "physics_type";
	public static final String BULLET = "physics_bullet";
	public static final String FIXED_ROT = "physics_fixedRot";
	public static final String LINEAR_DAMPING = "physics_linearDamping";
	public static final String ANGULAR_DAMPING = "physics_angularDamping";
	public static final String MASS = "physics_mass";
	public static final String FRICTION = "physics_friction";
	public static final String RESTITUTION = "physics_restitution";
	public static final String SHAPE = "physics_shape";

	private static final String[] IDENTIFIERS = new String[] { TYPE, BULLET, FIXED_ROT, LINEAR_DAMPING,
			ANGULAR_DAMPING, MASS, FRICTION, RESTITUTION };
	private Box2dPhysicsSystem m_physics;

	private Box2dPhysicsData m_data;

	// TODO: collision shape

	/**
	 * A default constructor which initializes a blank PhysicsData with no Shape.
	 * 
	 * @param physics
	 */
	public CBox2dPhysics() {
		m_data = new Box2dPhysicsData();
	}

	/**
	 * Initializes the CPhysics with a Shape.
	 * 
	 * @param shape
	 */
	public CBox2dPhysics(Shape shape) {
		m_data = new Box2dPhysicsData();
		m_data.setShape(shape);
	}

	/**
	 * Initializes the CPhysics with PhysicsData.
	 * 
	 * @param data
	 */
	public CBox2dPhysics(Box2dPhysicsData data) {
		m_data = new Box2dPhysicsData(data);
	}

	/**
	 * Sets the PhysicsSystem managing this CPhysics.
	 * 
	 * @param physics
	 */
	protected void setPhysicsSystem(Box2dPhysicsSystem physics) {
		m_physics = physics;
	}

	/**
	 * @return the PhysicsData that it was initialized with.
	 */
	protected Box2dPhysicsData getPhysicsData() {
		return m_data;
	}

	/**
	 * Applies a force to the center of the object. Forces are calculated by multiplying with the timestep (as opposed
	 * to impulses, which are instantaneous). This will do nothing unless the Entity has been added to the world.
	 * 
	 * @param force
	 */
	public void applyForce(Vector2f force) {
		Body body = m_physics.getBody(this);
		if (m_physics != null)
			body.applyForceToCenter(new Vec2(force.getX(), force.getY()));
	}

	/**
	 * Applies an impulse to the center of the object. Impulses are not time dependant (as opposed to forces, which are
	 * multiplied with the time step). This will do nothing unless the Entity has been added to the world.
	 * 
	 * @param impulse
	 */
	public void applyImpulse(Vector2f impulse) {
		Body body = m_physics.getBody(this);
		if (m_physics != null)
			body.applyLinearImpulse(new Vec2(impulse.getX(), impulse.getY()), body.getWorldCenter());
	}

	/**
	 * Applies a torque. This will do nothing unless the Entity has been added to the world.
	 * 
	 * @param torque
	 */
	public void applyTorque(float torque) {
		Body body = m_physics.getBody(this);
		if (m_physics != null)
			body.applyTorque(torque);
	}

	/**
	 * @return the BodyType - static, kinematic, or dynamic
	 */
	public BodyType getType() {
		if (m_physics == null)
			return m_data.getType();
		return m_physics.getBody(this).getType();
	}

	/**
	 * Sets the BodyType - static, kinematic, or dynamic.
	 * 
	 * @param type
	 */
	public void setType(BodyType type) {
		if (m_physics == null)
			m_data.setType(type);
		m_physics.getBody(this).setType(type);
	}

	/**
	 * @return whether the CPhysics is a bullet (does continuous collision detection).
	 */
	public boolean isBullet() {
		if (m_physics == null)
			return m_data.isBullet();
		return m_physics.getBody(this).isBullet();
	}

	/**
	 * Sets whether the CPhysics is a bullet (does continuous collision detection).
	 * 
	 * @param bullet
	 */
	public void setBullet(boolean bullet) {
		if (m_physics == null)
			m_data.setBullet(bullet);
		m_physics.getBody(this).setBullet(bullet);
	}

	/**
	 * @return whether the rotation is fixed
	 */
	public boolean isFixedRotation() {
		if (m_physics == null)
			return m_data.isFixedRotation();
		return m_physics.getBody(this).isFixedRotation();
	}

	/**
	 * Sets whether the rotation is fixed. Good for characters.
	 * 
	 * @param isRotationFixed
	 */
	public void setFixedRotation(boolean isRotationFixed) {
		if (m_physics == null)
			m_data.setFixedRotation(isRotationFixed);
		m_physics.getBody(this).setFixedRotation(isRotationFixed);
	}

	/**
	 * @return whether sleeping is allowed
	 */
	public boolean allowSleep() {
		if (m_physics == null)
			return m_data.allowSleep();

		return m_physics.getBody(this).isSleepingAllowed();
	}

	/**
	 * Sets whether sleeping is allowed.
	 * 
	 * @param allowSleep
	 */
	public void setAllowSleep(boolean allowSleep) {
		if (m_physics == null)
			m_data.setAllowSleep(allowSleep);

		m_physics.getBody(this).setSleepingAllowed(allowSleep);
	}

	/**
	 * @return the linear dampening - simulates "drag" or air resistance
	 */
	public float getLinearDamping() {
		if (m_physics == null)
			return m_data.getLinearDamping();
		return m_physics.getBody(this).getLinearDamping();
	}

	/**
	 * Sets the linear dampening - simulates "drag" or air resistance
	 * 
	 * @param damping
	 */
	public void setLinearDamping(float damping) {
		if (m_physics == null)
			m_data.setLinearDamping(damping);
		m_physics.getBody(this).setLinearDamping(damping);
	}

	/**
	 * @return the angular dampening - slows down an object's rotation
	 */
	public float getAngularDamping() {
		if (m_physics == null)
			return m_data.getAngularDamping();
		return m_physics.getBody(this).getAngularDamping();
	}

	/**
	 * Sets the angular dampening - slows down an object's rotation.
	 * 
	 * @param damping
	 */
	public void setAngularDamping(float damping) {
		if (m_physics == null)
			m_data.setAngularDamping(damping);
		m_physics.getBody(this).setAngularDamping(damping);
	}

	/**
	 * @return the gravity scale
	 */
	public float getGravityScale() {
		if (m_physics == null)
			return m_data.getGravityScale();
		return m_physics.getBody(this).getGravityScale();
	}

	/**
	 * Sets the gravity scale.
	 * 
	 * @param gravityScale
	 */
	public void setGravityScale(float gravityScale) {
		if (m_physics == null)
			m_data.setGravityScale(gravityScale);

		m_physics.getBody(this).setGravityScale(gravityScale);
	}

	/**
	 * @return the mass
	 */
	public float getMass() {
		if (m_physics == null)
			return m_data.getMass();
		Body body = m_physics.getBody(this);

		MassData data = new MassData();
		body.getMassData(data);
		return data.mass;
	}

	/**
	 * Sets the mass.
	 * 
	 * @param mass
	 */
	public void setMass(float mass) {
		if (m_physics == null)
			m_data.setMass(mass);
		Body body = m_physics.getBody(this);

		MassData data = new MassData();
		body.getMassData(data);
		data.mass = mass;
		body.setMassData(data);
	}

	/**
	 * @return the collision friction of the first fixture
	 */
	public float getFriction() {
		if (m_physics == null)
			return m_data.getFriction();
		Body body = m_physics.getBody(this);
		Fixture f = body.getFixtureList();

		return f.getFriction();
	}

	/**
	 * Sets the collision friction.
	 * 
	 * @param friction
	 */
	public void setFriction(float friction) {
		if (m_physics == null)
			m_data.setFriction(friction);

		Body body = m_physics.getBody(this);
		for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.getNext()) {
			fixture.setFriction(friction);
		}
	}

	/**
	 * @return the restitution, or "bounciness" of the first fixture
	 */
	public float getRestitution() {
		if (m_physics == null)
			return m_data.getRestitution();
		Body body = m_physics.getBody(this);
		Fixture f = body.getFixtureList();

		return f.getRestitution();
	}

	/**
	 * Sets the restitution, or "bounciness" of an object.
	 * 
	 * @param restitution
	 */
	public void setRestitution(float restitution) {
		if (m_physics == null)
			m_data.setRestitution(restitution);
		Body body = m_physics.getBody(this);
		Fixture f = body.getFixtureList();

		f.setRestitution(restitution);
		/*for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.getNext()) {
			fixture.setRestitution(restitution);
		}*/
	}

	/**
	 * @return the collision shape
	 */
	public Shape getShape() {
		if (m_physics == null)
			return m_data.getShape();
		return m_physics.getBody(this).getFixtureList().getShape();
	}

	/**
	 * Sets the collision shape - this is not yet implemented.
	 * 
	 * @param shape
	 */
	public void setShape(Shape shape) {
		if (m_physics == null)
			m_data.setShape(shape);

		Logger.instance().warn("Cannot change shape!");
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
		if (identifier.equals(TYPE))
			return getType();
		if (identifier.equals(BULLET))
			return isBullet();
		if (identifier.equals(FIXED_ROT))
			return isFixedRotation();
		if (identifier.equals(LINEAR_DAMPING))
			return getLinearDamping();
		if (identifier.equals(ANGULAR_DAMPING))
			return getAngularDamping();
		if (identifier.equals(MASS))
			return getMass();
		if (identifier.equals(FRICTION))
			return getFriction();
		if (identifier.equals(RESTITUTION))
			return getRestitution();
		if (identifier.equals(SHAPE))
			return getShape();

		throw new ComponentException("No such identifier!");
	}

	@Override
	public void setData(String identifier, Object data) {
		if (identifier == null)
			throw new ComponentException("Cannot set data for null identifier!");
		if (data == null)
			throw new ComponentException("Cannot set null data for identifier: " + identifier);

		if (identifier.equals(TYPE)) {
			setType((BodyType) data);
		} else if (identifier.equals(BULLET)) {
			setBullet((Boolean) data);
		} else if (identifier.equals(FIXED_ROT)) {
			setFixedRotation((Boolean) data);
		} else if (identifier.equals(LINEAR_DAMPING)) {
			setLinearDamping((Float) data);
		} else if (identifier.equals(ANGULAR_DAMPING)) {
			setAngularDamping((Float) data);
		} else if (identifier.equals(MASS)) {
			setMass((Float) data);
		} else if (identifier.equals(FRICTION)) {
			setFriction((Float) data);
		} else if (identifier.equals(RESTITUTION)) {
			setRestitution((Float) data);
		} else if (identifier.equals(SHAPE)) {
			setShape((Shape) data);
		} else {
			throw new ComponentException("No data for identifier: " + identifier);
		}
	}

	@Override
	public ComponentBuilder<CBox2dPhysics> getBuilder() {
		ComponentBuilder<CBox2dPhysics> builder = new ComponentBuilder<CBox2dPhysics>() {
			@Override
			public CBox2dPhysics build() {
				return new CBox2dPhysics(new Box2dPhysicsData(CBox2dPhysics.this));
			}

			@Override
			public String getName() {
				return NAME;
			}
		};
		return builder;
	}
}
