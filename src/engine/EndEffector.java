package engine;

import math.*;
import engine.physics.*;

public abstract class EndEffector extends DrawnObject
{	
	private boolean dragging;
	private Vec3 targetPosition;
	
	// =======================================================================
	// Properties
	// =======================================================================
	public Vec3 getTargetPosition()
	{
		if (targetPosition == null)
			targetPosition = getWorldPosition().xyz();
		return targetPosition;
	}
	
	// =======================================================================
	// Initialization
	// =======================================================================	
	public EndEffector(Vec3 initialPosition, Mesh mesh)
	{
		super(initialPosition, mesh);
		collider = createCollider();		
	}
	
	// =======================================================================
	// Game Object Methods
	// =======================================================================	
	public void handleClick(int mouseButton)
	{
		dragging = true;
	}
	
	public void update()
	{
		if (Input.mouseButtonReleased(0))
			dragging = false;
		
		if (dragging)
		{
//			Vec3 localPos = getLocalPosition().xyz();
//			Vec2 mouseDelta = Input.mouseDelta();
//			Vec3 deltaRay = Raycaster.mouseDeltaRay(mouseDelta);
//			Vec4 localDeltaRay = 
//					Mat4.mul(Glm.inverse(getWorldTransform()), 
//							new Vec4(deltaRay, 0));
//			targetPosition = localPos.add(localDeltaRay.xyz());
			
			Vec2 mouseDelta = Input.mouseDelta();
			Vec3 deltaRay = Raycaster.mouseDeltaRay(mouseDelta);
			targetPosition = getWorldPosition().xyz().add(deltaRay);
		}
		else
		{
			targetPosition = getWorldPosition().xyz();
		}
	}
	
	// =======================================================================
	// Subclass Sandbox Methods
	// =======================================================================	
	protected abstract Collider createCollider();
}
