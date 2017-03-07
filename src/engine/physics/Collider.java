package engine.physics;

import engine.GameObject;
import math.*;

/**
 * Parent class for all colliders.
 */
public abstract class Collider
{
	protected GameObject parentObject;

	// =======================================================================
	//  Initialization
	// =======================================================================
	public Collider(GameObject parentObject)
	{
		this.parentObject = parentObject;
	}
	
	// =======================================================================
	//  Intersection Tests
	// =======================================================================
	public final Collision intersects(Collider otherCollider)
	{
		if (otherCollider instanceof AABoxCollider)
			return intersects((AABoxCollider) otherCollider);
		else if (otherCollider instanceof SphereCollider)
			return intersects((SphereCollider) otherCollider);
		else
			return null;
	}
		
	public abstract Collision intersects(AABoxCollider otherCollider);
	
	public abstract Collision intersects(SphereCollider otherCollider);
	
	// =======================================================================
	//  Ray Casting
	// =======================================================================
	public abstract RaycastHit rayHit(Vec3 ray);

}
