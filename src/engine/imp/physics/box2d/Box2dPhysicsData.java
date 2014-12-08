package engine.imp.physics.box2d;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.BodyType;

/**
 * Contains all the data to initialize a CPhysics.
 */
public class Box2dPhysicsData {
	private BodyType m_type = BodyType.DYNAMIC;
	private boolean m_bullet = false;
	private boolean m_fixedRotation = false;
	private boolean m_allowSleep = true;
	private float m_linearDamping = 0f;
	private float m_angularDamping = 0f;
	private float m_gravityScale = 1f;
	private float m_mass = 1f;
	private float m_friction = 0.1f;
	private float m_restitution = 0.1f;
	private Shape m_shape = null;

	/**
	 * Creates an empty PhysicsData with all fields initialized to default values.
	 */
	public Box2dPhysicsData() {

	}

	/**
	 * Clones fields of a CPhysics.
	 * 
	 * @param physics
	 */
	public Box2dPhysicsData(CBox2dPhysics physics) {
		m_type = physics.getType();
		m_bullet = physics.isBullet();
		m_fixedRotation = physics.isFixedRotation();
		m_linearDamping = physics.getLinearDamping();
		m_angularDamping = physics.getAngularDamping();
		m_mass = physics.getMass();
		m_friction = physics.getFriction();
		m_restitution = physics.getRestitution();
		m_shape = physics.getShape();
	}

	/**
	 * Clones fields of a PhysicsData.
	 * 
	 * @param physics
	 */
	public Box2dPhysicsData(Box2dPhysicsData physics) {
		m_type = physics.getType();
		m_bullet = physics.isBullet();
		m_fixedRotation = physics.isFixedRotation();
		m_linearDamping = physics.getLinearDamping();
		m_angularDamping = physics.getAngularDamping();
		m_mass = physics.getMass();
		m_friction = physics.getFriction();
		m_restitution = physics.getRestitution();
		m_shape = physics.getShape();
	}

	/**
	 * @return the BodyType - static, kinematic, or dynamic.
	 */
	public BodyType getType() {
		return m_type;
	}

	/**
	 * Sets the BodyType - static, kinematic, or dynamic.
	 * 
	 * @param type
	 */
	public void setType(BodyType type) {
		m_type = type;
	}

	/**
	 * @return whether the object is a bullet (does continuous collision detection).
	 */
	public boolean isBullet() {
		return m_bullet;
	}

	/**
	 * Sets whether the object is a bullet (does continuous collision detection).
	 * 
	 * @param bullet
	 */
	public void setBullet(boolean bullet) {
		m_bullet = bullet;
	}

	/**
	 * @return whether the rotation is fixed
	 */
	public boolean isFixedRotation() {
		return m_fixedRotation;
	}

	/**
	 * Sets whether the rotation is fixed. Good for characters.
	 * 
	 * @param fixedRotation
	 */
	public void setFixedRotation(boolean fixedRotation) {
		m_fixedRotation = fixedRotation;
	}

	/**
	 * @return whether sleeping is allowed
	 */
	public boolean allowSleep() {
		return m_allowSleep;
	}

	/**
	 * Sets whether sleeping is allowed.
	 * 
	 * @param allowSleep
	 */
	public void setAllowSleep(boolean allowSleep) {
		m_allowSleep = allowSleep;
	}

	/**
	 * @return the linear dampening - simulates "drag" or air resistance
	 */
	public float getLinearDamping() {
		return m_linearDamping;
	}

	/**
	 * Sets the linear dampening - simulates "drag" or air resistance
	 * 
	 * @param linearDamping
	 */
	public void setLinearDamping(float linearDamping) {
		m_linearDamping = linearDamping;
	}

	/**
	 * @return the angular dampening - slows down an object's rotation
	 */
	public float getAngularDamping() {
		return m_angularDamping;
	}

	/**
	 * Sets the angular dampening - slows down an object's rotation.
	 * 
	 * @param angularDamping
	 */
	public void setAngularDamping(float angularDamping) {
		m_angularDamping = angularDamping;
	}

	/**
	 * @return the gravity scale
	 */
	public float getGravityScale() {
		return m_gravityScale;
	}

	/**
	 * Sets the gravity scale.
	 * 
	 * @param gravityScale
	 */
	public void setGravityScale(float gravityScale) {
		m_gravityScale = gravityScale;
	}

	/**
	 * @return the mass
	 */
	public float getMass() {
		return m_mass;
	}

	/**
	 * Sets the mass.
	 * 
	 * @param mass
	 */
	public void setMass(float mass) {
		m_mass = mass;
	}

	/**
	 * @return the collision friction of the first fixture
	 */
	public float getFriction() {
		return m_friction;
	}

	/**
	 * Sets the collision friction.
	 * 
	 * @param friction
	 */
	public void setFriction(float friction) {
		m_friction = friction;
	}

	/**
	 * @return the restitution, or "bounciness" of the first fixture
	 */
	public float getRestitution() {
		return m_restitution;
	}

	/**
	 * Sets the restitution, or "bounciness" of an object.
	 * 
	 * @param restitution
	 */
	public void setRestitution(float restitution) {
		m_restitution = restitution;
	}

	/**
	 * @return the collision shape
	 */
	public Shape getShape() {
		return m_shape;
	}

	/**
	 * Sets the collision shape - this is not yet implemented.
	 * 
	 * @param shape
	 */
	public void setShape(Shape shape) {
		m_shape = shape;
	}
}
