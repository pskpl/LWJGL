package engine.physics;

import engine.*;
import math.*;

/**
 * Encapsulates information about a collision with another collider.
 */
public class Collision
{
	public final Collider otherCollider;
	public final GameObject otherObject;
	public final Vec3 reflectionVector;
	
	// =======================================================================
	// Initialization
	// =======================================================================
	public Collision(Collider otherCollider, GameObject otherObject)
	{
		this.otherCollider = otherCollider;
		this.otherObject = otherObject;
		this.reflectionVector = null;
	}
	
	public Collision(Collider otherCollider, GameObject otherObject,
			Vec3 reflectionVector)
	{
		this.otherCollider = otherCollider;
		this.otherObject = otherObject;
		this.reflectionVector = reflectionVector;
	}
}
