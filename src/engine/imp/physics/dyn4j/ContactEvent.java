package engine.imp.physics.dyn4j;

import org.dyn4j.dynamics.contact.ContactConstraint;

import engine.core.Entity;

/**
 * Holds data about a collision.
 */
public class ContactEvent {
	public Entity entity1;
	public Entity entity2;
	public ContactConstraint contact;

	public ContactEvent(Entity entity1, Entity entity2, ContactConstraint contact) {
		this.entity1 = entity1;
		this.entity2 = entity2;
		this.contact = contact;
	}
}
