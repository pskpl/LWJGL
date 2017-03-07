package engine.physics;

import java.util.HashMap;

import engine.*;
import math.*;

/**
 * An axis-aligned bounding box.
 */
public class AABoxCollider extends Collider
{	
	private float width;
	private float height;
	private float depth;
	
	// =======================================================================
	// Property Accessors
	// =======================================================================
	Vec3 getMin()
	{
		Vec3 position = parentObject.getWorldPosition().xyz();
		return new Vec3(position.x - width / 2,
							position.y - height / 2,
							position.z - depth / 2);
	}
	
	Vec3 getMax()
	{
		Vec3 position = parentObject.getWorldPosition().xyz();
		return new Vec3(position.x + width / 2,
		                    position.y + height / 2,
		                    position.z + depth / 2);
	}
	
	// =======================================================================
	// Initialization
	// =======================================================================
	public AABoxCollider(GameObject parentObject,
			float width, float height, float depth)
	{
		super(parentObject);
		this.width = width;
		this.height = height;
		this.depth = depth;
	}
	
	// =======================================================================
	//  Intersection Tests
	// =======================================================================
	public Collision intersects(AABoxCollider otherCollider)
	{
		Vec3 collisionSurface = collisionSurfaceWithBox(otherCollider);
		if (collisionSurface != null)
		{
			Collision collision = new Collision(otherCollider,
					otherCollider.parentObject, collisionSurface.scale(-1));
			return collision;
		}
		
		return null;
	}

	public Collision intersects(SphereCollider otherCollider)
	{
		// TODO Implement.
		return null;
	}
	
	// =======================================================================
	//  Raycasting
	// =======================================================================
	public RaycastHit rayHit(Vec3 ray)
	{
		// TODO Implement.
		return null;
	}
	
	// =======================================================================
	// Helper Methods
	// =======================================================================
	// returns the normal of the surface that collided, or null if no
	// intersection exists
	private Vec3 collisionSurfaceWithBox(AABoxCollider otherBox)
	{
		Vec3 min = getMin();
		Vec3 max = getMax();
		Vec3 otherMin = otherBox.getMin();
		Vec3 otherMax = otherBox.getMax();
			
		HashMap<Vec3, Float> overlapMap = new HashMap<>();
		overlapMap.put(new Vec3(1, 0, 0), max.x - otherMin.x);
		overlapMap.put(new Vec3(-1, 0, 0), otherMax.x - min.x);
		overlapMap.put(new Vec3(0, 1, 0), max.y - otherMin.y);
		overlapMap.put(new Vec3(0, -1, 0), otherMax.y - min.y);
		overlapMap.put(new Vec3(0, 0, 1), max.z - otherMin.z);
		overlapMap.put(new Vec3(0, 0, -1), otherMax.z - min.z);
		return findCollisionSurface(overlapMap);
	}
	
	// finds the collision vector by looking for the smallest positive overlap
	// returns null if there is no intersection
	// one assumption made here is that the distance an object can move in one
	// frame is a tiny fraction of the dimensions of the colliding objects
	private Vec3 findCollisionSurface(HashMap<Vec3, Float> overlapMap)
	{
		float minPositive = Float.MAX_VALUE;
		Vec3 collisionSurface = null;
		
		for (Vec3 surface : overlapMap.keySet())
		{
			float value = overlapMap.get(surface);
			if (value < 0)	// means there is no overlap in a dimension
				return null;
			
			if (value < minPositive)
			{
				minPositive = value;
				collisionSurface = surface;
			}
		}
		
		return collisionSurface;
	}
}
