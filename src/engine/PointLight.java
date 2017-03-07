package engine;

import org.lwjgl.input.Keyboard;

import math.*;

public class PointLight extends Node
{
	public float moveSpeed = 0.5f; // world units per second
	public float rotationSpeed = 30; // degrees per second
	
	private Vec3 targetPosition;
	private Vec3 relativePositionSpherical;	// <r, theta, phi>
	private Color color;
	private SphericalControlScheme controlScheme;
	
	// =======================================================================
	// Properties
	// =======================================================================
	public Color getColor()
	{
		return color;
	}
	
	// =======================================================================
	// Initialization
	// =======================================================================
	public PointLight(Vec3 targetPosition, Vec3 relativePositionSpherical,
			Color color, SphericalControlScheme controlScheme)
	{
		super(Utility.sphericalToEuclidean(targetPosition, 
										   relativePositionSpherical));
		this.targetPosition = targetPosition;
		this.relativePositionSpherical = relativePositionSpherical;
		this.color = color;
		this.controlScheme = controlScheme;
	}
	
	// =======================================================================
	// Game Object Methods
	// =======================================================================
	public void update()
	{
		float dt = RunLoop.getDeltaTime();
		
		if (controlScheme.isControlDown(SphericalControlScheme.Control.IN))
			relativePositionSpherical.x += moveSpeed * dt;
		if (controlScheme.isControlDown(SphericalControlScheme.Control.OUT))
			relativePositionSpherical.x -= moveSpeed * dt;
		
		if (controlScheme.isControlDown(SphericalControlScheme.Control.ROT_THETA))
			relativePositionSpherical.y += rotationSpeed * dt;
		if (controlScheme.isControlDown(SphericalControlScheme.Control.UNROT_THETA))
			relativePositionSpherical.y -= rotationSpeed * dt;
		
		if (controlScheme.isControlDown(SphericalControlScheme.Control.ROT_PHI))
			relativePositionSpherical.z += rotationSpeed * dt;
		if (controlScheme.isControlDown(SphericalControlScheme.Control.UNROT_PHI))
			relativePositionSpherical.z -= rotationSpeed * dt;
		
		relativePositionSpherical.z = 
				Utility.clamp(relativePositionSpherical.z, 1, 179);
		
		setLocalPosition(Utility.sphericalToEuclidean(targetPosition, 
				relativePositionSpherical));	
		
//		Debug.log(this, relativePositionSpherical.toString());
	}
}
