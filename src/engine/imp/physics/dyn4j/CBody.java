package engine.imp.physics.dyn4j;

import org.dyn4j.collision.Filter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.Mass.Type;
import org.dyn4j.geometry.Rectangle;

import commons.BiMap;
import commons.matrix.Vector2f;

import engine.core.Component;
import engine.core.ComponentBuilder;
import engine.core.Entity;
import engine.imp.physics.PhysicsException;
import engine.imp.physics.PhysicsUtils;

/**
 * Contains data for the PhysicsSystem (Dyn4j backend). This code was adapted from the Phage2d project.
 */
public class CBody implements Component {
	public static final String NAME = "body";

	private Body m_body;
	private BodyFixture m_fixture;

	private CollisionFilter m_filter = new CollisionFilter() {
		@Override
		public boolean canCollide(Entity entity1, Entity entity2) {
			return true;
		}

		@Override
		public boolean continueCollision(Entity entity1, Entity entity2, ContactConstraint constraint) {
			return true;
		}
	};

	private static BiMap<Filter, CBody> s_allFilters = new BiMap<Filter, CBody>();

	/**
	 * Creates a CDyn4jBody.
	 * 
	 * @param def
	 */
	public CBody() {
		m_body = new Body();

		setShape(new Rectangle(1, 1));
		setMassType(Type.NORMAL);
		setGravityScale(1);
		setDensity(1);
	}

	/**
	 * Creates a CDyn4jPhysics with a certain shape.
	 * 
	 * @param shape
	 * 
	 * @param def
	 */
	public CBody(Convex shape) {
		m_body = new Body();

		setShape(shape);
		setMassType(Type.NORMAL);
		setGravityScale(1);
		setDensity(1);
	}

	/**
	 * @return the Body
	 */
	protected Body getBody() {
		return m_body;
	}

	/**
	 * Sets the collision filter to be used.
	 * 
	 * @param filter
	 */
	public void setCollisionFilter(CollisionFilter filter) {
		if (filter == null)
			throw new PhysicsException("Cannot set null CollisionFilter!");
		m_filter = filter;
	}

	/**
	 * @return the collision filter to be used
	 */
	public CollisionFilter getCollisionFilter() {
		return m_filter;
	}

	/**
	 * Returns whether this object is asleep in the physics engine.
	 * 
	 * @return
	 */
	public boolean isAsleep() {
		return m_body.isAsleep();
	}

	/**
	 * Sets whether this object is asleep in the physics engine.
	 * 
	 * @param asleep
	 */
	public void setAsleep(boolean asleep) {
		m_body.setAsleep(asleep);
	}

	/**
	 * Gets the movement friction/drag.
	 * 
	 * @param friction
	 * @return
	 */
	public float getLinearDamping() {
		return (float) m_body.getLinearDamping();
	}

	/**
	 * Sets the movement friction/drag.
	 * 
	 * @param friction
	 */
	public void setLinearDamping(float friction) {
		m_body.setLinearDamping(friction);
	}

	/**
	 * Gets the rotational friction/drag.
	 * 
	 * @param friction
	 * @return
	 */
	public float getAngularDamping() {
		return (float) m_body.getAngularDamping();
	}

	/**
	 * Sets the rotational friction/drag.
	 * 
	 * @param friction
	 */
	public void setAngularDamping(float friction) {
		m_body.setAngularDamping(friction);
	}

	/**
	 * Gets the friction when colliding with other objects.
	 * 
	 * @param friction
	 * @return
	 */
	public float getCollisionFriction() {
		return (float) m_fixture.getFriction();
	}

	/**
	 * Sets the friction when colliding with other objects.
	 * 
	 * @param friction
	 */
	public void setCollisionFriction(float friction) {
		m_fixture.setFriction(friction);
	}

	/**
	 * Gets the restitution, or elasticity.
	 * 
	 * @return
	 */
	public float getRestitution() {
		return (float) m_fixture.getRestitution();
	}

	/**
	 * Sets the restitution, or elasticity.
	 * 
	 * @param restitution
	 */
	public void setRestitution(float restitution) {
		m_fixture.setRestitution(restitution);
	}

	/**
	 * Returns the linear velocity of this object.
	 * 
	 * @return
	 */
	public Vector2f getVelocity() {
		return (PhysicsUtils.fromDyn4j(m_body.getLinearVelocity()));
	}

	/**
	 * Sets the linear velocity of this object.
	 * 
	 * @param velocity
	 */
	public void setVelocity(Vector2f velocity) {
		m_body.setLinearVelocity(velocity.getX(), velocity.getY());
		m_body.setAsleep(false);
	}

	/**
	 * Sets the x component of the linear velocity.
	 * 
	 * @param velocityX
	 */
	public void setVelocityX(float velocityX) {
		m_body.setLinearVelocity(velocityX, m_body.getLinearVelocity().y);
		m_body.setAsleep(false);
	}

	/**
	 * Sets the y component of the linear velocity.
	 * 
	 * @param velocityY
	 */
	public void setVelocityY(float velocityY) {
		m_body.setLinearVelocity(m_body.getLinearVelocity().y, velocityY);
		m_body.setAsleep(false);
	}

	/**
	 * Returns the angular, or rotational, velocity of this object.
	 * 
	 * @return
	 */
	public float getRotationalVelocity() {
		return (float) m_body.getAngularVelocity();
	}

	/**
	 * Sets the angular, or rotational, velocity of this object.
	 * 
	 * @param rotationalVelocity
	 */
	public void setRotationalVelocity(float rotationalVelocity) {
		m_body.setAngularVelocity(rotationalVelocity);
		m_body.setAsleep(false);
	}

	/**
	 * Get the effect gravity has on this object - 0 is no gravity, 1 is normal gravity.
	 * 
	 * @param mt
	 * @return
	 */
	public float getGravityScale() {
		return (float) m_body.getGravityScale();
	}

	/**
	 * Set the effect of gravity on the object - 0 is no gravity, 1 is normal gravity.
	 * 
	 * @param gravity
	 */
	public void setGravityScale(float gravity) {
		m_body.setGravityScale(gravity);
	}

	/**
	 * Gets the density of the object.
	 * 
	 * @return
	 */
	public float getDensity() {
		return (float) m_fixture.getDensity();
	}

	/**
	 * Recalculates the mass and inertia given the density.
	 * 
	 * @param density
	 */
	public void setDensity(float density) {
		Mass.Type type = getMassType();
		m_fixture.setDensity(density);
		m_body.setMass(m_fixture.createMass());
		setMassType(type);
	}

	/**
	 * Returns the mass of the object.
	 * 
	 * @return
	 */
	public float getMass() {
		return (float) m_body.getMass().getMass();
	}

	/**
	 * Sets the mass of this object.
	 * 
	 * @param mass
	 */
	public void setMass(float mass) {
		Mass massObj = new Mass(m_body.getMass().getCenter(), mass, m_body.getMass().getInertia());
		massObj.setType(m_body.getMass().getType());
		m_body.setMass(massObj);
	}

	/**
	 * Returns the intertia of this object.
	 * 
	 * @return
	 */
	public float getInertia() {
		return (float) m_body.getMass().getInertia();
	}

	/**
	 * Sets the inertia of this object.
	 * 
	 * @param inertia
	 */
	public void setInertia(float inertia) {
		Mass mass = new Mass(m_body.getMass().getCenter(), m_body.getMass().getMass(), inertia);
		mass.setType(m_body.getMass().getType());
		m_body.setMass(mass);
	}

	/**
	 * @return the mass type
	 */
	public Mass.Type getMassType() {
		return m_body.getMass().getType();
	}

	/**
	 * Sets the mass type
	 * 
	 * @param mt
	 */
	public void setMassType(Mass.Type mt) {
		Mass mass = new Mass(m_body.getMass().getCenter(), m_body.getMass().getMass(), m_body.getMass().getInertia());
		mass.setType(mt);
		m_body.setMass(mass);
	}

	/**
	 * returns if this structure is of type bullet - see setBullet(boolean isBullet).
	 * 
	 * @return
	 */
	public boolean isBullet() {
		return m_body.isBullet();
	}

	/**
	 * A bullet body is one that moves very quickly - therefore, continuous collision detection must be performed on all
	 * other bodies every step to avoid missing a collision - this is very expensive and should not be used unless
	 * necessary.
	 * 
	 * @param isBullet
	 */
	public void setBullet(boolean isBullet) {
		m_body.setBullet(isBullet);
	}

	/**
	 * Returns the offset of the center from the middle of the object. This determines the point around which the object
	 * will rotate.
	 * 
	 * @return
	 */
	public Vector2f getCenter() {
		return PhysicsUtils.fromDyn4j(m_body.getMass().getCenter());
	}

	/**
	 * Sets the offset of the center from the middle of the object. This determines the point around which the object
	 * will rotate.
	 * 
	 * @param center
	 */
	public void setCenter(Vector2f center) {
		m_body.setMass(new Mass(PhysicsUtils.toDyn4j(center), m_body.getMass().getMass(), m_body.getMass().getInertia()));
	}

	/**
	 * Gets the collision shape.
	 * 
	 * @return
	 */
	public Convex getShape() {
		return m_fixture.getShape();
	}

	/**
	 * Sets the collision shape. This is a relatively expensive call, so don't use it frequently.
	 * 
	 * @param convex
	 */
	public void setShape(Convex convex) {
		if (m_fixture != null) {
			float oldMass = getMass();
			float oldInertia = getInertia();
			float friction = getCollisionFriction();
			float restitution = getRestitution();

			m_body.removeAllFixtures();
			m_fixture = new BodyFixture(convex);
			m_fixture.setFriction(friction);
			m_fixture.setRestitution(restitution);
			m_body.addFixture(m_fixture);

			setMass(oldMass);
			setInertia(oldInertia);
		} else {
			m_fixture = new BodyFixture(convex);
			m_body.addFixture(m_fixture);
			m_body.setMass(m_fixture.createMass());
		}

		if (s_allFilters.containsValue(this)) {
			s_allFilters.removeBackward(this);
		}

		Filter filter = new Filter() {
			@Override
			public boolean isAllowed(Filter filter) {
				CBody cbody1 = s_allFilters.getForward(this);
				CBody cbody2 = s_allFilters.getForward(filter);
				Entity entity1 = (Entity) cbody1.getBody().getUserData();
				Entity entity2 = (Entity) cbody2.getBody().getUserData();

				if (!cbody1.getCollisionFilter().canCollide(entity1, entity2))
					return false;

				if (!cbody2.getCollisionFilter().canCollide(entity2, entity1))
					return false;

				return true;
			}
		};
		s_allFilters.put(filter, this);
		m_fixture.setFilter(filter);
	}

	/**
	 * Will apply a force to the structure - this will be processed upon the update by the physics engine.
	 * 
	 * @param force
	 */
	public void applyForce(Vector2f force) {
		m_body.applyImpulse(PhysicsUtils.toDyn4j(force));
	}

	/**
	 * Applies a torque around the center.
	 * 
	 * @param torque
	 */
	public void applyTorque(float torque) {
		m_body.applyTorque(torque);
	}

	/**
	 * Tests whether the body contains these coordinates in its translated convex.
	 * 
	 * @param worldCoordinates
	 * @return
	 */
	public boolean contains(Vector2f worldCoordinates) {
		return m_body.contains(PhysicsUtils.toDyn4j(worldCoordinates));
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public ComponentBuilder<CBody> getBuilder() {
		ComponentBuilder<CBody> builder = new ComponentBuilder<CBody>() {
			@Override
			public CBody build() {
				CBody newBody = new CBody(getShape());
				newBody.setCollisionFilter(getCollisionFilter());
				newBody.setAngularDamping(getAngularDamping());
				newBody.setAsleep(isAsleep());
				newBody.setBullet(isBullet());
				newBody.setCenter(getCenter());
				newBody.setCollisionFriction(getCollisionFriction());
				newBody.setGravityScale(getGravityScale());
				newBody.setMass(getMass());
				newBody.setInertia(getInertia());
				newBody.setMassType(getMassType());
				newBody.setLinearDamping(getLinearDamping());
				newBody.setAngularDamping(getAngularDamping());
				newBody.setRestitution(getRestitution());
				newBody.setCollisionFilter(getCollisionFilter());
				return newBody;
			}

			@Override
			public String getName() {
				return NAME;
			}
		};
		return builder;
	}
}
