package engine;

import math.*;

/**
 * A joint that can be rotated around a single rotation axis.
 */
public class Joint extends Node
{	
	public float rotationSpeed = 45; // degrees per second
	
	private Vec3 localRotationAxis;
	private float rotationDegrees;
	private float minRotation;
	private float maxRotation;
			
	// =======================================================================
	// Properties
	// =======================================================================
	public Vec3 getWorldRotationAxis()
	{
		return Mat4.mul(getWorldTransform(), 
						new Vec4(localRotationAxis, 0)).xyz();
	}
	
	public float getRotationDegrees()
	{
		return rotationDegrees;
	}
		
	// =======================================================================
	// Initialization
	// =======================================================================
	public Joint(Vec3 initialPosition, float minRotation, float maxRotation, 
					Vec3 localRotationAxis)
	{
		super(initialPosition);
		
		this.localRotationAxis = localRotationAxis;
		this.minRotation = minRotation;
		this.maxRotation = maxRotation;				
	}
	
	// =======================================================================
	// Revolution
	// =======================================================================
	public void rotateTo(float rotationDegrees)
	{
		float lastRotationDegrees = this.rotationDegrees;
		this.rotationDegrees = 
				Utility.clamp(rotationDegrees, minRotation, maxRotation);
		localRotate(this.rotationDegrees - lastRotationDegrees, 
				localRotationAxis);
	}
	
	public void rotateBy(float rotationDegrees)
	{
		rotateTo(this.rotationDegrees + rotationDegrees);
	}
}