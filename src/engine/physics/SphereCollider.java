package engine.physics;

import engine.*;
import math.*;

public class SphereCollider extends Collider
{
	private float radius;
	
	// =======================================================================
	// Initialization
	// =======================================================================
	public SphereCollider(GameObject gameObject, float radius)
	{
		super(gameObject);
		this.radius = radius;
	}

	// =======================================================================
	//  Intersection Tests
	// =======================================================================
	public Collision intersects(AABoxCollider otherCollider)
	{
		// TODO Implement.
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
		Vec3 negCenter = parentObject.getWorldPosition().xyz().scale(-1);
		
		float a = Glm.dot(ray, ray);
		float b = 2 * Glm.dot(ray, negCenter);
		float c = Glm.dot(negCenter, negCenter) - ((float) Math.pow(radius, 2));
		
		float discriminant = ((float) Math.pow(b, 2)) - 4 * a * c;
		if (discriminant < 0)
			return null;
		
		Vec2 solutions = quadFormula(a, b, c, discriminant);
		return new RaycastHit(parentObject, Math.min(solutions.x, solutions.y));
	}
	
	private Vec2 quadFormula(float a, float b, float c, float discriminant)
	{
		double first = (-b + Math.sqrt(discriminant)) / 2 * a;
		double second = (-b - Math.sqrt(discriminant)) / 2 * a;
		return new Vec2((float) first, (float) second);
	}
}
