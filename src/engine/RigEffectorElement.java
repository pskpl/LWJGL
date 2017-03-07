package engine;

import math.*;

public class RigEffectorElement extends RigElement
{
	private EndEffector effector;
	
	// =======================================================================
	// Properties
	// =======================================================================
	public Vec3 getTargetPosition() 
	{
		return effector.getTargetPosition();
	}
	
	// =======================================================================
	// Initialization
	// =======================================================================
	public RigEffectorElement(EndEffector effector)
	{
		super(effector);
		this.effector = effector;
	}
}
